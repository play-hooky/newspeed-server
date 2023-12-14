package com.newspeed.domain.content.application

import com.newspeed.domain.content.api.request.ContentSearchRequest
import com.newspeed.domain.content.api.response.ContentSearchResponse
import com.newspeed.domain.content.domain.enums.QueryPlatform
import org.springframework.stereotype.Service

@Service
class InstagramSearchService: ContentSearchClient {
    override fun getQueryPlatform(): QueryPlatform = QueryPlatform.INSTAGRAM

    override fun search(
        request: ContentSearchRequest
    ): ContentSearchResponse {
        TODO("Not yet implemented")
    }
}