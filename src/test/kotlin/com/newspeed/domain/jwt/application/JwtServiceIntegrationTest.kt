package com.newspeed.domain.jwt.application

import com.newspeed.domain.auth.domain.enums.Role
import com.newspeed.domain.jwt.domain.RefreshToken
import com.newspeed.domain.jwt.repository.RefreshTokenRepository
import com.newspeed.factory.auth.AuthFactory
import com.newspeed.factory.auth.AuthFactory.Companion.createAuthPayload
import com.newspeed.global.exception.auth.NotFoundTokenException
import com.newspeed.template.IntegrationTestTemplate
import io.jsonwebtoken.JwtParser
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import java.time.LocalDateTime
import java.time.ZoneOffset

@DisplayName("JwtService 계층 내")
class JwtServiceIntegrationTest(
    @Value("\${jwt.refresh-token-expiry-seconds}")
    val refreshTokenExpirySeconds: Long,
    @Value("\${jwt.access-token-expiry-seconds}")
    val accessTokenExpirySeconds: Long,
): IntegrationTestTemplate {

    @Autowired
    private lateinit var jwtService: JwtService

    @Autowired
    private lateinit var jwtParser: JwtParser

    @Autowired
    private lateinit var refreshTokenRepository: RefreshTokenRepository

    @Nested
    inner class `issueAllJwt 메서드에서` {

        @Test
        fun `refresh,access token 모두 발행한다`() {
            // given
            val authPayload = AuthFactory.createAuthPayload(Role.USER)
            val now = LocalDateTime.now().toEpochSecond(ZoneOffset.ofHours(9))

            // when
            val actual = jwtService.issueAllJwt(authPayload)

            // then
            Assertions.assertThat(jwtParser.isSigned(actual.accessToken)).isTrue()
            Assertions.assertThat(jwtParser.isSigned(actual.refreshToken)).isTrue()
            Assertions.assertThat(jwtParser.parseClaimsJws(actual.refreshToken).body.expiration.time / 1000 - now >= refreshTokenExpirySeconds).isTrue()
            Assertions.assertThat(jwtParser.parseClaimsJws(actual.accessToken).body.expiration.time / 1000 - now >= accessTokenExpirySeconds).isTrue()
        }
    }

    @Nested
    inner class `saveRefreshToken 메서드에서` {

        @Test
        fun `refreshToken을 저장한다`() {
            // given
            val userId = 1L
            val refreshToken = "hookyhookyhooky"

            // when
            jwtService.saveRefreshToken(userId, refreshToken)

            // then
            Assertions.assertThat(refreshTokenRepository.findByToken(refreshToken)).isNotNull
        }
    }

    @Nested
    inner class `reissueAccessToken 메서드에서` {

        @Test
        fun `accessToken을 재발행한다` () {
            // given
            val authPayload = createAuthPayload(Role.USER)
            val now = LocalDateTime.now().toEpochSecond(ZoneOffset.ofHours(9))

            // when
            val actual = jwtService.reissueAccessToken(authPayload)

            // then
            Assertions.assertThat(jwtParser.isSigned(actual))
            Assertions.assertThat(jwtParser.parseClaimsJws(actual).body.expiration.time / 1000 - now >= accessTokenExpirySeconds).isTrue()
        }
    }

    @Nested
    inner class `deleteRefreshToken 메서드에서` {

        @Test
        fun `refreshToken을 삭제한다`() {
            // given
            val userId = 1L
            val refreshToken = refreshTokenRepository.save(
                RefreshToken(
                    userId = userId,
                    token = "hooky"
                )
            )

            // when
            jwtService.deleteRefreshToken(userId)

            // then
            Assertions.assertThat(refreshTokenRepository.findByToken(refreshToken.token)).isNull()
        }

        @Test
        fun `refreshToken을 조회할 수 없으면 에러를 던진다`() {
            // given
            val userId = 1L

            // when & then
            Assertions.assertThatThrownBy { jwtService.deleteRefreshToken(userId) }
                .isInstanceOf(NotFoundTokenException::class.java)
        }
    }
}