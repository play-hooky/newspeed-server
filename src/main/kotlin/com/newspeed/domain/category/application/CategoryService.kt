package com.newspeed.domain.category.application

import com.newspeed.domain.category.api.response.CategoryResponse
import com.newspeed.domain.category.api.response.toCategoryResponse
import com.newspeed.domain.category.application.command.CategoryDeleteCommand
import com.newspeed.domain.category.repository.CategoryRepository
import com.newspeed.domain.user.application.UserService
import com.newspeed.global.exception.category.NotFoundCategoryException
import com.newspeed.global.exception.category.UnavailableDeleteCategoryException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CategoryService(
    private val categoryRepository: CategoryRepository,
    private val userService: UserService
) {

    @Transactional(readOnly = true)
    fun getCategories(
        userId: Long
    ): CategoryResponse {
        val user = userService.getUser(userId)

        return categoryRepository.findByUser(user)
            .map { it.toCategoryResponseDTO() }
            .toCategoryResponse()
    }

    fun deleteCategory(
        command: CategoryDeleteCommand
    ) {
        val user = userService.getUser(command.userId)
        val category = categoryRepository.findByIdOrNull(command.categoryId)
            ?: throw NotFoundCategoryException()

        category.takeIf { it.isCreatedBy(user) }
            ?.delete()
            ?: throw UnavailableDeleteCategoryException()
    }
}