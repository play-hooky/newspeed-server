package com.newspeed.domain.content.application

import com.newspeed.domain.content.domain.enums.QueryPlatform
import com.newspeed.domain.content.dto.ContentResponseDTO
import com.newspeed.domain.search.api.request.ContentSearchRequest

interface ContentSearchClient {

    fun getQueryPlatform(): QueryPlatform

    fun search(request: ContentSearchRequest): List<ContentResponseDTO>

    fun search(ids: List<String>): List<ContentResponseDTO>
}