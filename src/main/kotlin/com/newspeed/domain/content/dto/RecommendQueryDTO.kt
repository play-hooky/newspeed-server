package com.newspeed.domain.content.dto

import com.newspeed.domain.content.api.response.RecommendQueryResponse

data class RecommendQueryDTO(
    val query: String,
    val count: Long
) {
    constructor(): this(
        query = "",
        count = 0
    )
}

fun List<RecommendQueryDTO>.toRecommendQueryResponse() = RecommendQueryResponse(
    query = this.map { it.query }
)