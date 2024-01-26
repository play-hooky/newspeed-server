package com.newspeed.domain.auth.application

import com.newspeed.domain.auth.config.AppleOAuth2ConfigProperties
import com.newspeed.domain.auth.domain.enums.LoginPlatform
import com.newspeed.domain.auth.feign.AppleOAuth2TokenClient
import com.newspeed.domain.auth.feign.enums.AppleTokenTypeHint
import com.newspeed.domain.jwt.application.AppleJwtAuthExtractor
import com.newspeed.factory.auth.AuthFactory.Companion.createAppleJwtClaims
import com.newspeed.factory.auth.AuthFactory.Companion.createAppleOAuth2TokenRequest
import com.newspeed.factory.auth.AuthFactory.Companion.createAppleOAuth2TokenResponse
import com.newspeed.factory.auth.AuthFactory.Companion.createAppleOAuth2User
import com.newspeed.factory.auth.AuthFactory.Companion.createOAuth2UnlinkDTO
import com.newspeed.template.UnitTestTemplate
import org.assertj.core.api.SoftAssertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.*
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito

@DisplayName("애플 OAuth2 서비스에서 ")
class AppleOAuth2ServiceTest: UnitTestTemplate {
    @Mock
    private lateinit var tokenConfigProperties: AppleOAuth2ConfigProperties

    @Mock
    private lateinit var appleOAuth2TokenClient: AppleOAuth2TokenClient

    @Mock
    private lateinit var jwtAuthTokenExtractor: AppleJwtAuthExtractor

    @InjectMocks
    private lateinit var appleOAuth2Service: AppleOAuth2Service

    @Nested
    inner class `로그인 플랫폼을 조회하면` {

        @Test
        fun `애플을 조회한다`() {
            // given
            val expected = LoginPlatform.APPLE

            // when
            val actual = appleOAuth2Service.getLoginPlatform()

            // then
            assert(expected == actual)
        }
    }

    @Nested
    inner class `애플에 등록된 사용자 정보를 조회하면` {

        @Test
        fun 이메일_닉네임_프로필사진_조회에_성공한다() {
            // given
            val accessToken = "accessToken"
            val appleOAuth2TokenRequest = createAppleOAuth2TokenRequest()
            val appleOAuth2TokenResponse = createAppleOAuth2TokenResponse()
            val appleJwtClaims = createAppleJwtClaims()
            val expected = createAppleOAuth2User()

            given(tokenConfigProperties.toAppleOAuth2TokenRequest(accessToken))
                .willReturn(appleOAuth2TokenRequest)

            given(appleOAuth2TokenClient.getOAuthAppleToken(appleOAuth2TokenRequest.clientId, appleOAuth2TokenRequest.clientSecret, appleOAuth2TokenRequest.grantType, appleOAuth2TokenRequest.authorizationCode))
                .willReturn(appleOAuth2TokenResponse)

            given(jwtAuthTokenExtractor.getClaims(appleOAuth2TokenResponse.idToken))
                .willReturn(appleJwtClaims)

            // when
            val actual = appleOAuth2Service.getOAuth2User(accessToken)

            // then
            SoftAssertions.assertSoftly { softly: SoftAssertions ->
                softly.assertThat(actual.platform).isEqualTo(LoginPlatform.APPLE)
                softly.assertThat(actual.nickname).isEqualTo(expected.nickname)
                softly.assertThat(actual.profileImage).isEqualTo(expected.profileImage)
                softly.assertThat(actual.email).isEqualTo(expected.email)
            }
        }
    }

    @Nested
    inner class `애플에 등록된 사용자를 탈퇴하면` {

        @Test
        fun `애플에 회원 탈퇴를 요청한다`() {
            // given
            val appleOAuth2TokenRequest = createAppleOAuth2TokenRequest()
            val appleOAuth2TokenResponse = createAppleOAuth2TokenResponse()
            val appleJwtClaims = createAppleJwtClaims()
            val oAuth2UnlinkDTO = createOAuth2UnlinkDTO()

            given(tokenConfigProperties.toAppleOAuth2TokenRequest(oAuth2UnlinkDTO.authorizationCode))
                .willReturn(appleOAuth2TokenRequest)

            given(appleOAuth2TokenClient.getOAuthAppleToken(appleOAuth2TokenRequest.clientId, appleOAuth2TokenRequest.clientSecret, appleOAuth2TokenRequest.grantType, appleOAuth2TokenRequest.authorizationCode))
                .willReturn(appleOAuth2TokenResponse)

            given(jwtAuthTokenExtractor.getClaims(appleOAuth2TokenResponse.idToken))
                .willReturn(appleJwtClaims)

            doNothing().`when`(appleOAuth2TokenClient).revoke(appleOAuth2TokenRequest.clientId, appleOAuth2TokenRequest.clientSecret, oAuth2UnlinkDTO.authorizationCode, AppleTokenTypeHint.ACCESS_TOKEN.value)

            // when
            appleOAuth2Service.unlink(oAuth2UnlinkDTO)

            // then
            Mockito.verify(appleOAuth2TokenClient, times(1)).revoke(
                appleOAuth2TokenRequest.clientId,
                appleOAuth2TokenRequest.clientSecret,
                oAuth2UnlinkDTO.authorizationCode,
                AppleTokenTypeHint.ACCESS_TOKEN.value
            )
        }
    }
}