package com.newspeed.domain.category.application

import com.newspeed.domain.category.api.response.CategoryResponse
import com.newspeed.domain.category.application.command.CategoryDeleteCommand
import com.newspeed.domain.category.repository.CategoryRepository
import com.newspeed.domain.user.application.UserService
import com.newspeed.domain.user.domain.User
import com.newspeed.factory.auth.AuthFactory.Companion.createDummyUser
import com.newspeed.factory.auth.AuthFactory.Companion.createKakaoUser
import com.newspeed.factory.category.CategoryFactory.Companion.createCategory
import com.newspeed.global.exception.category.NotFoundCategoryException
import com.newspeed.global.exception.category.UnavailableDeleteCategoryException
import com.newspeed.template.UnitTestTemplate
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import org.springframework.data.repository.findByIdOrNull

@DisplayName("Category 서비스 계층에서")
class CategoryServiceTest: UnitTestTemplate {
    private val categoryRepository: CategoryRepository = mockk<CategoryRepository>(relaxed = true)
    private val userService: UserService = Mockito.mock(UserService::class.java)

    private val categoryService: CategoryService = CategoryService(
        categoryRepository = categoryRepository,
        userService = userService
    )

    @Nested
    inner class `Category를 조회하면` {

        @Test
        fun `Category 테이블에서 조회하여 제공한다`() {
            // given
            val user = createDummyUser()
            val category = createCategory(user)
            val categories = listOf(
                category
            )
            val expected = CategoryResponse(
                categories = listOf(
                    CategoryResponse.CategoryResponseDTO(
                        id = category.id,
                        category = category.name
                    )
                )
            )

            given(userService.getUser(user.id))
                .willReturn(user)

            every { categoryRepository.findByUser(user) } returns categories

            // when
            val actual = categoryService.getCategories(user.id)

            // then
            Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected)
        }
    }

    @Nested
    inner class `Category를 삭제할 때` {

        @Test
        fun `Category ID로 조회할 수 없으면 에러를 던진다`() {
            // given
            val user = createKakaoUser()
            val category = createCategory(user)
            val command = CategoryDeleteCommand(
                userId = user.id,
                categoryId = category.id
            )

            given(userService.getUser(user.id))
                .willReturn(user)

            every { categoryRepository.findByIdOrNull(category.id) } returns null

            // when & then
            Assertions.assertThatThrownBy { categoryService.deleteCategory(command) }
                .isInstanceOf(NotFoundCategoryException::class.java)
        }

        @Test
        fun `사용자의 Category가 아니면 에러를 던진다`() {
            // given
            val user = createKakaoUser()
            val wrongUser = User(
                id = user.id + 1,
                email = user.email,
                nickname = user.nickname,
                platform = user.platform,
                profileImageUrl = user.profileImageUrl,
                role = user.role
            )
            val category = createCategory(user)
            val command = CategoryDeleteCommand(
                userId = wrongUser.id,
                categoryId = category.id
            )

            given(userService.getUser(wrongUser.id))
                .willReturn(wrongUser)

            every { categoryRepository.findByIdOrNull(category.id) } returns category

            // when & then
            Assertions.assertThatThrownBy { categoryService.deleteCategory(command) }
                .isInstanceOf(UnavailableDeleteCategoryException::class.java)
        }

        @Test
        fun `Category 테이블을 조회하여 삭제한다`() {
            // given
            val user = createKakaoUser()
            val category = createCategory(user)
            val command = CategoryDeleteCommand(
                userId = user.id,
                categoryId = category.id
            )

            given(userService.getUser(user.id))
                .willReturn(user)

            every { categoryRepository.findByIdOrNull(category.id) } returns category

            // when
            categoryService.deleteCategory(command)

            // then
            assertTrue(category.deletedAt() != null)
        }
    }
}