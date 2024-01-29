package com.newspeed.domain.category.application.command

import javax.validation.constraints.Positive

data class CategoryDeleteCommand(
    @field:Positive(message = "사용자를 식별할 수 없습니다.")
    val userId: Long,
    @field:Positive(message = "해당 카테고리를 조회할 수 없습니다.")
    val categoryId: Long
) {
    companion object {
        fun of(
            userId: Long,
            categoryId: Long
        ): CategoryDeleteCommand = CategoryDeleteCommand(
            userId = userId,
            categoryId = categoryId
        )
    }
}
