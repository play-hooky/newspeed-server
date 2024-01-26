package com.newspeed.domain.category.api.response

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.newspeed.domain.category.application.serde.CategoryResponseSerializer

@JsonSerialize(using = CategoryResponseSerializer::class)
data class CategoryResponse(
    val categories: List<CategoryResponseDTO>
) {
    data class CategoryResponseDTO(
        val id: Long,
        val category: String
    )
}

fun List<CategoryResponse.CategoryResponseDTO>.toCategoryResponse(): CategoryResponse = CategoryResponse(
    categories = this
)