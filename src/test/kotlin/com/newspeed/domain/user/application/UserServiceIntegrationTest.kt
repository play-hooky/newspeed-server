package com.newspeed.domain.user.application

import com.newspeed.domain.auth.domain.enums.Role
import com.newspeed.domain.user.domain.User
import com.newspeed.domain.user.repository.UserRepository
import com.newspeed.factory.auth.AuthFactory
import com.newspeed.factory.auth.AuthFactory.Companion.createKakaoUser
import com.newspeed.global.exception.user.UserNotFoundException
import com.newspeed.template.IntegrationTestTemplate
import org.assertj.core.api.Assertions
import org.assertj.core.api.SoftAssertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDateTime

@DisplayName("UserService 계층 내 ")
class UserServiceIntegrationTest: IntegrationTestTemplate {

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var userRepository: UserRepository

    @Nested
    inner class `saveIfNotExist 메서드에서` {

        @Test
        fun `존재하면 DB를 조회하여 결과를 반환한다`() {
            // given
            val oAuth2User = AuthFactory.createKakaoOAuth2User()
            userRepository.save(
                User(
                    email = oAuth2User.email,
                    nickname = oAuth2User.nickname,
                    platform = oAuth2User.platform,
                    profileImageUrl = oAuth2User.profileImage,
                    role = Role.USER
                )
            )

            // when
            val actual = userService.saveIfNotExist(oAuth2User)

            // then
            SoftAssertions.assertSoftly { softly: SoftAssertions ->
                softly.assertThat(actual.email).isEqualTo(oAuth2User.email)
                softly.assertThat(actual.nickname).isEqualTo(oAuth2User.nickname)
                softly.assertThat(actual.platform).isEqualTo(oAuth2User.platform)
                softly.assertThat(actual.profileImageUrl).isEqualTo(oAuth2User.profileImage)
                softly.assertThat(actual.role).isEqualTo(Role.USER)
            }
        }

        @Test
        fun `존재하지 않으면 DB에 사용자를 저장한다`() {
            // given
            val oAuth2User = AuthFactory.createKakaoOAuth2User()

            // when
            val actual = userService.saveIfNotExist(oAuth2User)

            // then
            SoftAssertions.assertSoftly { softly: SoftAssertions ->
                softly.assertThat(actual.email).isEqualTo(oAuth2User.email)
                softly.assertThat(actual.nickname).isEqualTo(oAuth2User.nickname)
                softly.assertThat(actual.platform).isEqualTo(oAuth2User.platform)
                softly.assertThat(actual.profileImageUrl).isEqualTo(oAuth2User.profileImage)
                softly.assertThat(actual.role).isEqualTo(Role.USER)
            }
        }
    }

    @Nested
    inner class `사용자를 조회할 때` {

        @Test
        fun `사용자가 존재하지 않으면 에러를 던진다`() {
            // given
            val userId = Long.MIN_VALUE

            // when & then
            Assertions.assertThatThrownBy { userService.getUser(userId) }
                .isInstanceOf(UserNotFoundException::class.java)
        }

        @Test
        fun `사용자가 존재하면 DB로부터 결과를 반환한다`() {
            // given
            val user = userRepository.save(
                createKakaoUser()
            )

            // when
            val actual= userService.getUser(user.id)

            Assertions.assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFieldsOfTypes(LocalDateTime::class.java)
                .isEqualTo(user)
        }
    }
}