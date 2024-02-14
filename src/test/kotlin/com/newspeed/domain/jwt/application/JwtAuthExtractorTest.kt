package com.newspeed.domain.jwt.application

import com.newspeed.domain.auth.domain.enums.LoginPlatform
import com.newspeed.domain.auth.domain.enums.Role
import com.newspeed.global.exception.auth.ExpiredJwtException
import com.newspeed.global.exception.auth.InsufficientJwtClaimException
import com.newspeed.global.exception.auth.InvalidJwtException
import com.newspeed.global.exception.model.ExceptionType
import com.newspeed.template.UnitTestTemplate
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.assertj.core.api.SoftAssertions
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.nio.charset.StandardCharsets
import java.util.*

@DisplayName("JwtAuthExtractor 클래스에서 ")
class JwtAuthExtractorTest: UnitTestTemplate {
    companion object {
        val USER_CLAIM = Pair("userId", 1L)
        val ROLE_CLAIM = Pair("role", Role.USER)
        val PLATFORM_CLAIM = Pair("platform", LoginPlatform.NEWSPEED)
        val EMAIL_CLAIM = Pair("email", "playhooky@newspeed.store")

        private val key = "playhookyplayhookyplayhookyplayhookyplayhooky"
        private val secretKey = Keys.hmacShaKeyFor(key.toByteArray(StandardCharsets.UTF_8))

        private val jwtParser = Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()

        private val jwtAuthExtractor = JwtAuthExtractor(jwtParser)
    }

    @Nested
    inner class `JWT 추출은 ` {

        @Test
        fun `JWT 토큰 형식이 아니면 예외를 던진다`() {
            // given
            val token = "play hooky"

            // when & then
            assertThatThrownBy { jwtAuthExtractor.extract(token) }
                .isInstanceOf(InvalidJwtException::class.java)
        }

        @Test
        fun `기간이 만료된 토큰이면 예외를 던진다`() {
            // given
            val token = Jwts.builder()
                .setExpiration(Date(Date().time - 1000))
                .signWith(secretKey)
                .compact()

            // when & then
            assertThatThrownBy { jwtAuthExtractor.extract(token) }
                .isInstanceOf(ExpiredJwtException::class.java)
        }

        @Test
        fun `키가 유효하지 않으면 예외를 던진다`() {
            // given
            val otherKey = Keys.hmacShaKeyFor("a$key".toByteArray(StandardCharsets.UTF_8))
            val token = Jwts.builder()
                .setExpiration(Date(Date().time - 1000))
                .signWith(otherKey)
                .compact()

            // when & then
            assertThatThrownBy { jwtAuthExtractor.extract(token) }
                .isInstanceOf(InvalidJwtException::class.java)
        }

        @Test
        fun `token이 빈문자열이면 예외를 던진다`() {
            // given
            val token = ""

            // when & then
            assertThatThrownBy { jwtAuthExtractor.extract(token) }
                .isInstanceOf(InvalidJwtException::class.java)
        }

        @Test
        fun `userId가 존재하지 않으면 예외를 던진다`() {
            // given
            val token = Jwts.builder()
                .claim(USER_CLAIM.first, null)
                .claim(ROLE_CLAIM.first, ROLE_CLAIM.second)
                .claim(PLATFORM_CLAIM.first, PLATFORM_CLAIM.second)
                .claim(EMAIL_CLAIM.first, EMAIL_CLAIM.second)
                .setExpiration(Date(Date().time + 10000))
                .signWith(secretKey)
                .compact()

            // when & then
            assertThatThrownBy { jwtAuthExtractor.extract(token) }
                .hasMessage("userId${ExceptionType.INSUFFICIENT_JWT_CLAIM_EXCEPTION.message}")
        }

        @Test
        fun `role이 존재하지 않으면 예외를 던진다`() {
            // given
            val token = Jwts.builder()
                .claim(USER_CLAIM.first, USER_CLAIM.second)
                .claim(ROLE_CLAIM.first, null)
                .claim(PLATFORM_CLAIM.first, PLATFORM_CLAIM.second)
                .claim(EMAIL_CLAIM.first, EMAIL_CLAIM.second)
                .setExpiration(Date(Date().time + 1000))
                .signWith(secretKey)
                .compact()

            // when & then
            assertThatThrownBy { jwtAuthExtractor.extract(token) }
                .isInstanceOf(InsufficientJwtClaimException::class.java)
                .hasMessage("role${ExceptionType.INSUFFICIENT_JWT_CLAIM_EXCEPTION.message}")
        }

        @Test
        fun `로그인 플랫폼이 존재하지 않으면 예외를 던진다`() {
            // given
            val token = Jwts.builder()
                .claim(USER_CLAIM.first, USER_CLAIM.second)
                .claim(ROLE_CLAIM.first, ROLE_CLAIM.second)
                .claim(PLATFORM_CLAIM.first, null)
                .claim(EMAIL_CLAIM.first, EMAIL_CLAIM.second)
                .setExpiration(Date(Date().time + 1000))
                .signWith(secretKey)
                .compact()

            // when & then
            assertThatThrownBy { jwtAuthExtractor.extract(token) }
                .isInstanceOf(InsufficientJwtClaimException::class.java)
                .hasMessage("loginPlatform${ExceptionType.INSUFFICIENT_JWT_CLAIM_EXCEPTION.message}")
        }

        @Test
        fun `이메일이 존재하지 않으면 예외를 던진다`() {
            // given
            val token = Jwts.builder()
                .claim(USER_CLAIM.first, USER_CLAIM.second)
                .claim(ROLE_CLAIM.first, ROLE_CLAIM.second)
                .claim(PLATFORM_CLAIM.first, PLATFORM_CLAIM.second)
                .claim(EMAIL_CLAIM.first, null)
                .setExpiration(Date(Date().time + 1000))
                .signWith(secretKey)
                .compact()

            // when & then
            assertThatThrownBy { jwtAuthExtractor.extract(token) }
                .isInstanceOf(InsufficientJwtClaimException::class.java)
                .hasMessage("email${ExceptionType.INSUFFICIENT_JWT_CLAIM_EXCEPTION.message}")
        }

        @Test
        fun `정상 JWT면 추출에 성공한다`() {
            // given
            val token = Jwts.builder()
                .claim(USER_CLAIM.first, USER_CLAIM.second)
                .claim(ROLE_CLAIM.first, ROLE_CLAIM.second)
                .claim(PLATFORM_CLAIM.first, PLATFORM_CLAIM.second)
                .claim(EMAIL_CLAIM.first, EMAIL_CLAIM.second)
                .setExpiration(Date(Date().time + 1000))
                .signWith(secretKey)
                .compact()

            // when
            val authPayload = jwtAuthExtractor.extract(token)

            // then
            assertSoftly{ softly : SoftAssertions ->
                softly.assertThat(authPayload.userId).isEqualTo(USER_CLAIM.second)
                softly.assertThat(authPayload.role).isEqualTo(ROLE_CLAIM.second)
                softly.assertThat(authPayload.loginPlatform).isEqualTo(PLATFORM_CLAIM.second)
                softly.assertThat(authPayload.email).isEqualTo(EMAIL_CLAIM.second)
            }
        }
    }
}