package com.newspeed.domain.content.application.serde

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.newspeed.domain.content.api.response.ContentsResponse

class ContentsResponseSerializer: StdSerializer<ContentsResponse>(ContentsResponse::class.java) {
    override fun serialize(
        responseBody: ContentsResponse,
        gen: JsonGenerator,
        provider: SerializerProvider
    ) {
        gen.writeStartArray()
        responseBody
            .contentResponses
            .forEach{ gen.writeObject(it) }
        gen.writeEndArray()
    }
}