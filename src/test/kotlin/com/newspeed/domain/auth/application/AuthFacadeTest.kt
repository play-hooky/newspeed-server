package com.newspeed.domain.auth.application

import com.newspeed.domain.jwt.repository.RefreshTokenRepository
import com.newspeed.domain.user.repository.UserRepository
import com.newspeed.factory.auth.AuthFactory
import com.newspeed.template.IntegrationTestTemplate
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
    inner class 카카오_로그인에 {

        @Test
        fun 성공하면_사용자_정보와_발급한_JWT를_저장한다() {
            // given
            val kakaoLoginRequest = AuthFactory.createKakaoLoginRequest()

            // when
            val actual = authFacade.kakaoLogin(kakaoLoginRequest)

            val actualUser = userRepository.findByIdOrNull(actual.userId)!!
            val actualRefreshToken = refreshTokenRepository.findByToken(actual.refreshToken)!!

            assertSoftly { softly: SoftAssertions ->
                softly.assertThat(actual.userId).isEqualTo(actualUser.id)
                softly.assertThat(actual.refreshToken).isEqualTo(actualRefreshToken.token)
            }
        }
    }
}