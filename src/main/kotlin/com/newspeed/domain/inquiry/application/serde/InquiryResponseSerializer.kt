package com.newspeed.domain.inquiry.application.serde

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.newspeed.domain.inquiry.api.response.InquiryResponse

class InquiryResponseSerializer: StdSerializer<InquiryResponse>(InquiryResponse::class.java) {
    override fun serialize(
        responseBody: InquiryResponse,
        gen: JsonGenerator,
        provider: SerializerProvider
    ) {
        gen.writeStartArray()
        responseBody
            .inquiries
            .forEach { gen.writeObject(it) }
        gen.writeEndArray()
    }
}