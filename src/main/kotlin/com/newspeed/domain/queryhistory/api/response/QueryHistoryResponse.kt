package com.newspeed.domain.queryhistory.api.response

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.newspeed.domain.content.application.serde.QueryHistoryResponseSerializer

@JsonSerialize(using = QueryHistoryResponseSerializer::class)
data class QueryHistoryResponse(
    val queryHistories: List<QueryHistory>
) {
    data class QueryHistory(
        val id: Long,
        val query: String
    )
}
