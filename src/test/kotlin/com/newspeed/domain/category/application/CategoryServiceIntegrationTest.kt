package com.newspeed.domain.category.application

import com.newspeed.domain.auth.domain.enums.LoginPlatform
import com.newspeed.domain.auth.domain.enums.Role
import com.newspeed.domain.category.application.command.CategoryDeleteCommand
import com.newspeed.domain.category.domain.Category
import com.newspeed.domain.category.repository.CategoryRepository
import com.newspeed.domain.user.domain.User
import com.newspeed.domain.user.repository.UserRepository
import com.newspeed.factory.auth.AuthFactory.Companion.DUMMY_EMAIL
import com.newspeed.factory.auth.AuthFactory.Companion.DUMMY_NICKNAME
import com.newspeed.factory.auth.AuthFactory.Companion.createKakaoUser
import com.newspeed.factory.category.CategoryFactory.Companion.createCategory
import com.newspeed.global.exception.category.NotFoundCategoryException
import com.newspeed.global.exception.category.UnavailableDeleteCategoryException
import com.newspeed.template.IntegrationTestTemplate
import org.assertj.core.api.Assertions
import org.assertj.core.api.SoftAssertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull

@DisplayName("Category Service 계층 내")
class CategoryServiceIntegrationTest: IntegrationTestTemplate {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var categoryService: CategoryService

    @Autowired
    private lateinit var categoryRepository: CategoryRepository

    private lateinit var user: User

    private lateinit var category: Category

    @BeforeEach
    fun setup() {
        user = userRepository.save(
            createKakaoUser()
        )

        category = saveCategories(user)
    }

    @Nested
    inner class `Category를 조회할 때` {

        @Test
        fun `DB에서 조회하여 결과를 반환한다`() {
            // given
            val userId = user.id

            // when
            val actual = categoryService.getCategories(userId)

            // then
            SoftAssertions.assertSoftly { softly: SoftAssertions ->
                softly.assertThat(actual.categories[0].category).isEqualTo(category.name)
                softly.assertThat(actual.categories[0].id).isEqualTo(category.id)
            }
        }
    }

    @Nested
    inner class `Category를 삭제할 때` {

        @Test
        fun `Category가 존재하지 않으면 에러를 던진다`() {
            // given
            val command = CategoryDeleteCommand(
                userId = user.id,
                categoryId = Long.MAX_VALUE
            )

            // when & then
            Assertions.assertThatThrownBy { categoryService.deleteCategory(command) }
                .isInstanceOf(NotFoundCategoryException::class.java)
        }

        @Test
        fun `해당 사용자가 아니면 에러를 던진다`() {
            // given
            val wrongUser = userRepository.save(
                User(
                    id = 2L,
                    email = DUMMY_EMAIL,
                    nickname = DUMMY_NICKNAME,
                    platform = LoginPlatform.NEWSPEED,
                    profileImageUrl = null,
                    role = Role.USER
                )
            )

            val command = CategoryDeleteCommand(
                userId = wrongUser.id,
                categoryId = category.id
            )

            // when & then
            Assertions.assertThatThrownBy { categoryService.deleteCategory(command) }
                .isInstanceOf(UnavailableDeleteCategoryException::class.java)
        }

        @Test
        fun `DB에서 조회하여 Category를 삭제한다`() {
            // given
            val command = CategoryDeleteCommand(
                userId = user.id,
                categoryId = category.id
            )

            // when
            categoryService.deleteCategory(command)

            // then
            Assertions.assertThat(categoryRepository.findByIdOrNull(category.id)).isNull()
        }
    }

    private fun saveCategories(
        user: User
    ): Category = categoryRepository.save(
        createCategory(user)
    )
}