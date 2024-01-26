package com.newspeed.domain.category.api

import com.newspeed.domain.category.api.response.CategoryResponse
import com.newspeed.domain.category.application.CategoryService
import com.newspeed.domain.jwt.annotation.User
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/contents/categories")
class CategoryController(
    private val categoryService: CategoryService
) {

    @GetMapping
    fun getCategories(
        @User userId: Long
    ): ResponseEntity<CategoryResponse> = ResponseEntity.ok(
        categoryService.getCategories(userId)
    )
}