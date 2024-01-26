package com.newspeed.domain.auth.application

import com.newspeed.domain.auth.application.command.WithdrawalCommand
import com.newspeed.domain.jwt.domain.RefreshToken
import com.newspeed.domain.jwt.repository.RefreshTokenRepository
import com.newspeed.domain.user.domain.User
import com.newspeed.domain.user.repository.UserRepository
import com.newspeed.factory.auth.AuthFactory
import com.newspeed.factory.auth.AuthFactory.Companion.createDummyUser
import com.newspeed.factory.auth.AuthFactory.Companion.createKakaoUser
import com.newspeed.global.exception.user.UserNotFoundException
import com.newspeed.template.IntegrationTestTemplate
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.assertj.core.api.SoftAssertions
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull

@DisplayName("auth facade 내 ")
class AuthFacadeTest: IntegrationTestTemplate {

    @Autowired
    private lateinit var authFacade: AuthFacade

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var refreshTokenRepository: RefreshTokenRepository

    @Nested
    inner class `카카오 로그인에` {

        @Test
        fun `성공하면 사용자 정보와 발급한 JWT를 저장한다`() {
            // given
            val kakaoLoginRequest = AuthFactory.createKakaoLoginRequest()

            // when
            val actual = authFacade.login(kakaoLoginRequest)

            val actualUser = userRepository.findByIdOrNull(actual.userId)!!
            val actualRefreshToken = refreshTokenRepository.findByToken(actual.refreshToken)!!

            assertSoftly { softly: SoftAssertions ->
                softly.assertThat(actual.userId).isEqualTo(actualUser.id)
                softly.assertThat(actual.refreshToken).isEqualTo(actualRefreshToken.token)
            }
        }
    }

    @Nested
    inner class `사용자를 조회할 때` {

        @Test
        fun `userId가 유효하면 사용자 정보를 반환한다`() {
            // given
            val kakaoLoginRequest = AuthFactory.createKakaoLoginRequest()
            val loginResponse = authFacade.login(kakaoLoginRequest)
            val expected = userRepository.findByIdOrNull(loginResponse.userId)!!

            // when
            val actual = authFacade.getUserResponse(expected.id)

            // then
            assertSoftly { softly: SoftAssertions ->
                softly.assertThat(actual.email).isEqualTo(expected.email)
                softly.assertThat(actual.nickname).isEqualTo(expected.nickname)
                softly.assertThat(actual.profileImgUrl).isEqualTo(expected.profileImageUrl)
            }
        }

        @Test
        fun `userId가 존재하지 않는 사용자라면 예외를 던진다`() {
            assertThatThrownBy { authFacade.getUserResponse(Long.MIN_VALUE) }
                .isInstanceOf(UserNotFoundException::class.java)
        }
    }

    @Nested
    inner class `access token을 재발행할 때` {

        @Test
        fun `userId가 유효하면 access token 발행에 성공한다`() {
            // given
            val kakaoLoginRequest = AuthFactory.createKakaoLoginRequest()
            val loginResponse = authFacade.login(kakaoLoginRequest)
            val user = userRepository.findByIdOrNull(loginResponse.userId)!!

            // when
            val actual = authFacade.reissueAccessToken(user.id)

            assertThat(actual).isNotBlank()
        }

        @Test
        fun `userId가 존재하지 않는 사용자라면 예외를 던진다`() {
            assertThatThrownBy { authFacade.reissueAccessToken(Long.MIN_VALUE) }
                .isInstanceOf(UserNotFoundException::class.java)
        }
    }

    @Nested
    inner class `로그아웃할 때` {

        @Test
        fun `refresh token을 삭제한다`() {
            // given
            val userId = 1L
            val refreshToken = refreshTokenRepository.save(
                RefreshToken(
                    userId = userId,
                    token = "hookyhookyhooky"
                )
            )

            // when
            authFacade.logout(userId)

            // then
            assertThat(refreshTokenRepository.findByUserId(userId)).isEmpty()
        }
    }

    @Nested
    inner class `회원 탈퇴 할 때` {

        @Test
        fun `user와 refresh token을 삭제하고 oauth 해제한다`() {
            // given
            val user = saveUser()
            val refreshToken = saveRefreshToken(user)
            val command = WithdrawalCommand(
                authorizationCode = "hooky",
                userId = user.id
            )

            // when
            authFacade.unlink(command)

            // then
            assertThat(refreshTokenRepository.findByUserId(user.id)).isEmpty()
            assertThat(userRepository.findByIdOrNull(user.id)).isNull()
        }
    }

    private fun saveUser(): User = userRepository.save(
        createDummyUser()
    )

    private fun saveRefreshToken(
        user: User
    ): RefreshToken = refreshTokenRepository.save(
        RefreshToken(
            userId = user.id,
            token = "token"
        )
    )
}