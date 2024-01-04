package com.newspeed.domain.content.api.response

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.newspeed.domain.content.application.serde.RecommendQueryResponseSerializer

@JsonSerialize(using = RecommendQueryResponseSerializer::class)
data class RecommendQueryResponse(
    val query: List<String>
)
