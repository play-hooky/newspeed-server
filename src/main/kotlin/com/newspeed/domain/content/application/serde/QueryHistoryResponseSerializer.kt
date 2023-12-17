package com.newspeed.domain.content.application.serde

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.newspeed.domain.content.api.response.QueryHistoryResponse

class QueryHistoryResponseSerializer: StdSerializer<QueryHistoryResponse>(QueryHistoryResponse::class.java) {
    override fun serialize(
        responseBody: QueryHistoryResponse,
        gen: JsonGenerator,
        provider: SerializerProvider
    ) {
        gen.writeStartArray()
        responseBody
            .queryHistories
            .forEach{ gen.writeObject(it) }
        gen.writeEndArray()
    }
}