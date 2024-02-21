package com.newspeed.factory.content

import com.newspeed.domain.content.application.ContentSearchClient
import com.newspeed.domain.content.domain.enums.QueryPlatform
import com.newspeed.domain.content.dto.ContentResponseDTO
import com.newspeed.domain.search.api.request.ContentSearchRequest
import org.springframework.stereotype.Service

@Service
class DummyContentClient: ContentSearchClient {
    override fun getQueryPlatform(): QueryPlatform = QueryPlatform.NEWSPEED

    override fun searchDetailBy(request: ContentSearchRequest): List<ContentResponseDTO> = ContentFactory.createContentResponseDTOs(QueryPlatform.NEWSPEED)

    override fun searchDetailBy(ids: List<String>): List<ContentResponseDTO> = ContentFactory.createContentResponseDTOs(QueryPlatform.NEWSPEED)
}