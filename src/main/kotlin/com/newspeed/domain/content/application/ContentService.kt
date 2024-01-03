package com.newspeed.domain.content.application

import com.newspeed.domain.content.api.request.ContentSearchRequest
import com.newspeed.domain.content.api.response.ContentSearchResponse
import com.newspeed.domain.content.api.response.QueryHistoryResponse
import com.newspeed.domain.content.domain.toQueryHistoryResponse
import com.newspeed.domain.content.repository.QueryHistoryRepository
import com.newspeed.domain.user.application.UserService
import com.newspeed.global.exception.content.NotFoundQueryHistoryException
import com.newspeed.global.exception.content.UnavailableDeleteQueryHistoryException
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.annotation.Validated

@Service
@Validated
class ContentService(
    private val contentSearchClients: ContentSearchClients,
    private val userService: UserService,
    private val queryHistoryRepository: QueryHistoryRepository,
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

    @Transactional(readOnly = true)
    fun getQueryHistory(
        userId: Long
    ): QueryHistoryResponse {
        val user = userService.getUser(userId)

        return queryHistoryRepository.findByUser(user)
            .toQueryHistoryResponse()
    }

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