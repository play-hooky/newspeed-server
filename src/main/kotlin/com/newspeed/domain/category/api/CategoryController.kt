package com.newspeed.domain.category.api

import com.newspeed.domain.category.api.response.CategoryResponse
import com.newspeed.domain.category.application.CategoryService
import com.newspeed.domain.category.application.command.CategoryDeleteCommand
import com.newspeed.domain.jwt.annotation.User
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

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

    @DeleteMapping("/{categoryId}")
    fun deleteCategory(
        @User userId: Long,
        @PathVariable("categoryId") categoryId: Long
    ): ResponseEntity<Unit> {
        categoryService.deleteCategory(
            CategoryDeleteCommand.of(userId, categoryId)
        )

        return ResponseEntity(HttpStatus.OK)
    }
}