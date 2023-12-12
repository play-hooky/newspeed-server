package com.newspeed.domain.content.application

import com.newspeed.domain.content.api.request.ContentSearchRequest
import com.newspeed.domain.content.api.response.ContentSearchResponse
import org.springframework.stereotype.Service
import org.springframework.validation.annotation.Validated

@Service
@Validated
class ContentService(
    private val contentSearchClients: ContentSearchClients
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
}