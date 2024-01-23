package com.newspeed.domain.search.application

import com.newspeed.domain.content.application.ContentSearchClients
import com.newspeed.domain.content.domain.enums.QueryPlatform
import com.newspeed.domain.content.dto.ContentResponseDTO
import com.newspeed.domain.search.api.request.ContentSearchRequest
import com.newspeed.domain.search.api.response.ContentSearchResponse
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service

@Service
class SearchService(
    private val contentSearchClients: ContentSearchClients,
    private val eventPublisher: ApplicationEventPublisher
) {
    fun search(
        request: ContentSearchRequest
    ): ContentSearchResponse {
        return contentSearchClients.getClient(request.platform)
            .search(request)
    }

    fun search(
        platform: QueryPlatform,
        ids: List<String>
    ): List<ContentResponseDTO> = contentSearchClients.getClient(platform)
        .search(ids)

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
}