package com.newspeed.domain.content.application

import com.newspeed.domain.content.api.response.ContentsResponse
import com.newspeed.domain.content.api.response.toContentsResponse
import com.newspeed.domain.content.application.command.ContentDeleteCommand
import com.newspeed.domain.content.application.command.ContentSaveCommand
import com.newspeed.domain.content.domain.toContentIdsInPlatform
import com.newspeed.domain.content.repository.ContentRepository
import com.newspeed.domain.search.application.SearchService
import com.newspeed.domain.user.application.UserService
import com.newspeed.global.exception.content.NotFoundContentException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.annotation.Validated
import javax.validation.Valid

@Service
@Validated
class ContentService(
    private val userService: UserService,
    private val searchService: SearchService,
    private val contentRepository: ContentRepository
) {

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
            .map { searchService.search(it.key, it.value.toContentIdsInPlatform()) }
            .flatten()
            .toContentsResponse()
    }

    @Transactional
    fun deleteContent(
        @Valid command: ContentDeleteCommand
    ) {
        val user = userService.getUser(command.userId)
        val contentIds = contentRepository.findByUserAndUrl(user, command.url)
            .takeIf { it.isNotEmpty() }
            ?.map { it.id }
            ?: throw NotFoundContentException()

        contentRepository.deleteByIds(contentIds)
    }
}