package com.newspeed.domain.search.api.response

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.newspeed.domain.content.application.serde.ContentSearchResponseSerializer
import com.newspeed.domain.content.dto.ContentResponseDTO

@JsonSerialize(using = ContentSearchResponseSerializer::class)
data class ContentSearchResponse(
    val contents: List<ContentResponseDTO>
)
