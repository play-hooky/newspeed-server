package com.newspeed.domain.content.application.serde

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.newspeed.domain.content.api.response.ContentSearchResponse

class ContentSearchResponseSerializer: StdSerializer<ContentSearchResponse>(ContentSearchResponse::class.java) {
    override fun serialize(
        responseBody: ContentSearchResponse,
        gen: JsonGenerator,
        provider: SerializerProvider
    ) {
        gen.writeStartArray()
        responseBody
            .contents
            .forEach{ gen.writeObject(it) }
        gen.writeEndArray()
    }
}