package com.newspeed.domain.category.application.serde

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.newspeed.domain.category.api.response.CategoryResponse

class CategoryResponseSerializer: StdSerializer<CategoryResponse>(CategoryResponse::class.java) {
    override fun serialize(
        responseBody: CategoryResponse,
        gen: JsonGenerator,
        provider: SerializerProvider
    ) {
       gen.writeStartArray()
       responseBody
           .categories
           .forEach { gen.writeObject(it) }
        gen.writeEndArray()
    }
}