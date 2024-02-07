package com.newspeed.domain.jwt.application

import com.fasterxml.jackson.databind.ObjectMapper
import com.newspeed.domain.auth.domain.AuthPayload
import com.newspeed.domain.auth.domain.enums.LoginPlatform
import com.newspeed.domain.auth.domain.enums.Role
import com.newspeed.domain.auth.feign.AppleOAuth2TokenClient
import com.newspeed.domain.auth.feign.response.AppleOAuth2PublicKeyResponse
import com.newspeed.template.UnitTestTemplate
import io.jsonwebtoken.Jwts
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock
import org.springframework.util.Base64Utils
import java.math.BigInteger
import java.security.KeyFactory
import java.security.PublicKey
import java.security.spec.RSAPublicKeySpec

@DisplayName("애플 JWT 파싱 객체 내")
class AppleJwtAuthExtractorTest: UnitTestTemplate {
    private val objectMapper = ObjectMapper()
    private val appleOAuth2TokenClient = mock(AppleOAuth2TokenClient::class.java)
    private val appleJwtAUthExtractor = AppleJwtAuthExtractor(
        objectMapper,
        appleOAuth2TokenClient
    )

    @Nested
    @Disabled
    inner class `extract 메서드는` {

        @Test
        fun `jwt를 사용자 인증 정보로 변환한다`() {
            // given
            val userId = 1L
            val role = Role.USER
            val loginPlatform = LoginPlatform.APPLE
            val email = "play-hooky@apple.co.kr"
            val appleOAuth2PublicKeyResponse = AppleOAuth2PublicKeyResponse(
                keys = listOf(AppleOAuth2PublicKeyResponse.Key(
                    keyType = "RSA",
                    keyId = "Bh6H7rHVmb",
                    usage = "sig",
                    algorithm = "RS256",
                    modulus = "2HkIZ7xKMUYH_wtt2Gwq6jXKRl-Ng5vdwd-XcWn5RIW82-uxdmGJyTo3T6MPty-xWUeW7FCs9NlM4yu02GKgwep7TKfnOovP78sd3rESbZsvuN7zD_Vk6aZP7QfqblElUtiMQxh9bu-gZUeMZfa_ndX-P5C4yKtZvXGrSPLLjyAcSmSHNLZnWbZXjeIVsgXWHMr5fwVEAkftHq_4py82xgn2XEK0FK9HmWOCZ47Wcx9fWBnqSi9JTJTUX0lh-kI5TcYfv9UKX2oe3uyOn-A460E_L_4ximlM-lgi3otw26EZfAGY9FFgSZoACjhgw_z5NRbK9dycHRpeLY9GxIyiYwr",
                    exponent = "AQAB"
                ))
            )
            val token = Jwts
                .builder()
                .signWith(generatePublicKey(appleOAuth2PublicKeyResponse.keys.get(0)))
                .compact()
            val expected = AuthPayload(
                userId = userId,
                role = role,
                loginPlatform = loginPlatform,
                email = email
            )

            given(appleOAuth2TokenClient.getApplePublicKeys())
                .willReturn(appleOAuth2PublicKeyResponse)

            // when
            val actual = appleJwtAUthExtractor.extract(token)

            // then
            assertThat(actual).usingRecursiveComparison().isEqualTo(expected)
        }
    }

    private fun generatePublicKey(
        applePublicKey: AppleOAuth2PublicKeyResponse.Key
    ): PublicKey {
        val publicKeyModulus = Base64Utils.decodeFromUrlSafeString(applePublicKey.modulus)
        val publicKeyExponent = Base64Utils.decodeFromUrlSafeString(applePublicKey.exponent)
        val publicKeySpec = RSAPublicKeySpec(BigInteger(1, publicKeyModulus), BigInteger(1, publicKeyExponent))

        return KeyFactory
            .getInstance(applePublicKey.keyType)
            .generatePublic(publicKeySpec)
    }
}