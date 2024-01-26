package com.newspeed.domain.category.application

import com.newspeed.domain.category.api.response.CategoryResponse
import com.newspeed.domain.category.domain.Category
import com.newspeed.domain.category.repository.CategoryRepository
import com.newspeed.domain.content.domain.enums.QueryPlatform
import com.newspeed.domain.user.application.UserService
import com.newspeed.factory.auth.AuthFactory.Companion.createDummyUser
import com.newspeed.template.UnitTestTemplate
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock

@DisplayName("Category 서비스 계층에서")
class CategoryServiceTest: UnitTestTemplate {

    @Mock
    private lateinit var categoryRepository: CategoryRepository

    @Mock
    private lateinit var userService: UserService

    @InjectMocks
    private lateinit var categoryService: CategoryService

    @Nested
    inner class `Category를 조회하면` {

        @Test
        fun `Category 테이블에서 조회하여 제공한다`() {
            // given
            val user = createDummyUser()
            val category = Category(
                user = user,
                name = "hooky",
                platform = QueryPlatform.YOUTUBE
            )
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

            given(categoryRepository.findByUser(user))
                .willReturn(categories)

            // when
            val actual = categoryService.getCategories(user.id)

            // then
            Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected)
        }
    }
}