package com.newspeed.domain.content.application

import com.newspeed.domain.content.api.request.ContentSearchRequest
import com.newspeed.domain.content.api.response.ContentSearchResponse
import com.newspeed.domain.content.domain.enums.QueryPlatform

interface ContentSearchClient {

    fun getQueryPlatform(): QueryPlatform

    fun search(request: ContentSearchRequest): ContentSearchResponse
}