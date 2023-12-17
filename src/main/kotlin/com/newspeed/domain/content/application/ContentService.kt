package com.newspeed.domain.content.application

import com.newspeed.domain.content.api.request.ContentSearchRequest
import com.newspeed.domain.content.api.response.ContentSearchResponse
import com.newspeed.domain.content.api.response.QueryHistoryResponse
import com.newspeed.domain.content.domain.toQueryHistoryResponse
import com.newspeed.domain.content.repository.QueryHistoryRepository
import com.newspeed.domain.user.application.UserService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.annotation.Validated

@Service
@Validated
class ContentService(
    private val contentSearchClients: ContentSearchClients,
    private val userService: UserService,
    private val queryHistoryRepository: QueryHistoryRepository
) {

    fun search(
        userId: Long,
        request: ContentSearchRequest
    ): ContentSearchResponse {
        return search(request)
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
}