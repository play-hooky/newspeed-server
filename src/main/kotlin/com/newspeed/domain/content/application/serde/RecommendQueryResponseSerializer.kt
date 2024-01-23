package com.newspeed.domain.content.application.serde

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.newspeed.domain.queryhistory.api.response.RecommendQueryResponse

class RecommendQueryResponseSerializer: StdSerializer<RecommendQueryResponse>(RecommendQueryResponse::class.java) {
    override fun serialize(
        responseBody: RecommendQueryResponse,
        gen: JsonGenerator,
        provider: SerializerProvider
    ) {
       gen.writeStartArray()
       responseBody
           .query
           .forEach{ gen.writeObject(it) }
        gen.writeEndArray()
    }
}