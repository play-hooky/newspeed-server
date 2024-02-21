package com.newspeed.domain.search.application

import com.newspeed.domain.content.application.ContentSearchClients
import com.newspeed.domain.content.domain.enums.QueryPlatform
import com.newspeed.domain.content.dto.ContentResponseDTO
import com.newspeed.domain.search.api.request.ContentSearchRequest
import com.newspeed.domain.search.api.response.ContentSearchResponse
import com.newspeed.domain.search.api.response.toContentSearchResponse
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service

@Service
class SearchService(
    private val contentSearchClients: ContentSearchClients,
    private val eventPublisher: ApplicationEventPublisher
) {
    fun search(
        request: ContentSearchRequest
    ): ContentSearchResponse = contentSearchClients.getClient(request.platform)
        .searchDetailBy(request)
        .toContentSearchResponse()

    fun search(
        platform: QueryPlatform,
        ids: List<String>
    ): List<ContentResponseDTO> = contentSearchClients.getClient(platform)
        .searchDetailBy(ids)

    fun search(
        userId: Long,
        request: ContentSearchRequest
    ): ContentSearchResponse {
        val searchResponse = search(request)
        publishSearchSuccessEvent(userId, request)
        return searchResponse
    }

    private fun publishSearchSuccessEvent(
        userId: Long,
        request: ContentSearchRequest
    ) {
        request.query?.let {
            eventPublisher.publishEvent(
                request.toContentSearchEvent(userId)
            )
        }
    }
}