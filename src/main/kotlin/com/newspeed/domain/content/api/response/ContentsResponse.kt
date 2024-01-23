package com.newspeed.domain.content.api.response

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.newspeed.domain.content.application.serde.ContentsResponseSerializer
import com.newspeed.domain.content.dto.ContentResponseDTO

@JsonSerialize(using = ContentsResponseSerializer::class)
data class ContentsResponse(
    val contentResponses: List<ContentResponseDTO>
)

fun List<ContentResponseDTO>.toContentsResponse() = ContentsResponse(
    contentResponses = this
)
