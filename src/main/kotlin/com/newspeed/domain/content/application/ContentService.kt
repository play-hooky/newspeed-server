package com.newspeed.domain.content.application

import com.newspeed.domain.content.api.request.ContentSearchRequest
import com.newspeed.domain.content.api.response.*
import com.newspeed.domain.content.application.command.ContentSaveCommand
import com.newspeed.domain.content.domain.enums.QueryPlatform
import com.newspeed.domain.content.domain.toContentIdsInPlatform
import com.newspeed.domain.content.domain.toQueryHistoryResponse
import com.newspeed.domain.content.dto.ContentResponseDTO
import com.newspeed.domain.content.dto.toRecommendQueryResponse
import com.newspeed.domain.content.repository.ContentRepository
import com.newspeed.domain.content.repository.QueryHistoryRepository
import com.newspeed.domain.user.application.UserService
import com.newspeed.global.exception.content.NotFoundQueryHistoryException
import com.newspeed.global.exception.content.UnavailableDeleteQueryHistoryException
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.annotation.Validated
import java.time.LocalDate
import javax.validation.Valid

@Service
@Validated
class ContentService(
    private val contentSearchClients: ContentSearchClients,
    private val userService: UserService,
    private val queryHistoryRepository: QueryHistoryRepository,
    private val contentRepository: ContentRepository,
    private val eventPublisher: ApplicationEventPublisher
) {

    fun search(
        userId: Long,
        request: ContentSearchRequest
    ): ContentSearchResponse {
        val searchResponse = search(request)

        request.query?.let {
            eventPublisher.publishEvent(
                request.toContentSearchEvent(userId)
            )
        }

        return searchResponse
    }

    fun search(
        request: ContentSearchRequest
    ): ContentSearchResponse {
        return contentSearchClients.getClient(request.platform)
            .search(request)
    }

    private fun search(
        platform: QueryPlatform,
        ids: List<String>
    ): List<ContentResponseDTO> = contentSearchClients.getClient(platform)
        .search(ids)

    @Transactional
    fun saveContent(
        @Valid command: ContentSaveCommand
    ) {
        val user = userService.getUser(command.userId)
        val content = command.toContentEntity(user)

        contentRepository.save(content)
    }

    @Transactional(readOnly = true)
    fun getContents(
        userId: Long
    ): ContentsResponse {
        val contents = contentRepository.findByUserId(userId)

        return contents
            .groupBy { it.platform }
            .map { search(it.key, it.value.toContentIdsInPlatform()) }
            .flatten()
            .toContentsResponse()
    }

    @Transactional(readOnly = true)
    fun getQueryHistory(
        userId: Long
    ): QueryHistoryResponse {
        val user = userService.getUser(userId)

        return queryHistoryRepository.findByUser(user)
            .toQueryHistoryResponse()
    }

    @Transactional(readOnly = true)
    fun recommendQuery(
        date: LocalDate,
        size: Int
    ): RecommendQueryResponse = queryHistoryRepository.findDailyMaxQueryHistory(date, size)
            .toRecommendQueryResponse()

    @Transactional
    fun deleteQueryHistory(
        userId: Long,
        queryHistoryId: Long
    ) {
        val user = userService.getUser(userId)
        val queryHistory = queryHistoryRepository.findByIdOrNull(queryHistoryId)
            ?: throw NotFoundQueryHistoryException()

        queryHistory.takeIf { it.isCreatedBy(user) }
            ?.delete()
            ?: throw UnavailableDeleteQueryHistoryException()
    }
}