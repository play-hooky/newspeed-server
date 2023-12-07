package com.newspeed.domain.jwt.application

import com.newspeed.domain.auth.domain.enums.Role
import com.newspeed.domain.jwt.domain.JwtConfigProperties
import com.newspeed.domain.jwt.repository.RefreshTokenRepository
import com.newspeed.factory.auth.AuthFactory.Companion.createAuthPayload
import com.newspeed.template.UnitTestTemplate
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import java.nio.charset.StandardCharsets

@DisplayName("JwtService 에서 ")
class JwtServiceTest: UnitTestTemplate {
    private val secretKey = Keys.hmacShaKeyFor("playhookyplayhookyplayhookyplayhookyplayhookyplayhooky".toByteArray(StandardCharsets.UTF_8))
    private val jwtAuthProvider = JwtAuthProvider(
        jwtConfigProperties = mock(JwtConfigProperties::class.java),
        secretKey = secretKey
    )

    private val refreshTokenRepository = mock(RefreshTokenRepository::class.java)

    private val jwtService = JwtService(
        jwtAuthProvider = jwtAuthProvider,
        refreshTokenRepository = refreshTokenRepository
    )

    @Nested
    inner class `access token을 재발행할 때` {

        @Test
        fun `올바른 payload이면 재발행에 성공한다`() {
            // given
            val authPayload = createAuthPayload(Role.USER)
            val jwtParser = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()

            // when
            val accessToken = jwtService.reissueAccessToken(authPayload)

            // then
            assertThat(jwtParser.isSigned(accessToken))
                .isTrue()
        }
    }
}