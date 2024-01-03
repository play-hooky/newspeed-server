package com.newspeed.factory.content

import com.newspeed.domain.content.api.request.ContentSearchRequest
import com.newspeed.domain.content.api.response.ContentSearchResponse
import com.newspeed.domain.content.application.ContentSearchClient
import com.newspeed.domain.content.domain.enums.QueryPlatform
import org.springframework.stereotype.Service

@Service
class DummyContentClient: ContentSearchClient {
    override fun getQueryPlatform(): QueryPlatform = QueryPlatform.NEWSPEED

    override fun search(request: ContentSearchRequest): ContentSearchResponse = ContentFactory.createContentSearchResponse()
}