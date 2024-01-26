package com.newspeed.domain.auth.application

import com.newspeed.domain.auth.config.KakaoOAuth2ConfigProperties
import com.newspeed.domain.auth.domain.enums.LoginPlatform
import com.newspeed.domain.auth.feign.KakaoOAuth2TokenClient
import com.newspeed.domain.auth.feign.KakaoOAuth2UserClient
import com.newspeed.factory.auth.AuthFactory
import com.newspeed.template.UnitTestTemplate
import org.assertj.core.api.SoftAssertions
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock

@DisplayName("카카오 OAuth2 서비스에서 ")
class KakaoOAuth2ServiceTest: UnitTestTemplate {
    @Mock
    private lateinit var tokenClient: KakaoOAuth2TokenClient

    @Mock
    private lateinit var configProperties: KakaoOAuth2ConfigProperties

    @Mock
    private lateinit var userClient: KakaoOAuth2UserClient

    @InjectMocks
    private lateinit var kakaoOAuth2Service: KakaoOAuth2Service

    @Nested
    inner class 로그인_플랫폼을_조회하면  {

        @Test
        fun 카카오를_조회한다() {
            // given
            val expected = LoginPlatform.KAKAO

            // when
            val actual = kakaoOAuth2Service.getLoginPlatform()

            // then
            assert(expected == actual)
        }
    }

    @Nested
    inner class 카카오에_등록된_사용자_정보를_조회하면 {

        @Test
        fun 이메일_닉네임_프로필사진_조회에_성공한다() {
            // given
            val accessToken = "accessToken"
            val kakaoOauth2TokenRequest = AuthFactory.createKakaoOauth2TokenRequest()
            val kakaoOAuth2TokenResponse = AuthFactory.createKakaoOAuth2TokenResponse()
            val kakaoOAuth2UserResponse = AuthFactory.createKakaoOAuth2UserResponse()
            val expected = AuthFactory.createKakaoOAuth2User()

            given(configProperties.toKakaoOAuth2TokenRequest(accessToken))
                .willReturn(kakaoOauth2TokenRequest)

            given(tokenClient.getOAuthKakaoToken(kakaoOauth2TokenRequest))
                .willReturn(kakaoOAuth2TokenResponse)

            given(userClient.getKakaoUserEntity(kakaoOAuth2TokenResponse.getAuthorizationHeader()))
                .willReturn(kakaoOAuth2UserResponse)

            // when
            val actual = kakaoOAuth2Service.getOAuth2User(accessToken)

            // then
            assertSoftly { softly: SoftAssertions ->
                softly.assertThat(actual.platform).isEqualTo(LoginPlatform.KAKAO)
                softly.assertThat(actual.nickname).isEqualTo(expected.nickname)
                softly.assertThat(actual.profileImage).isEqualTo(expected.profileImage)
                softly.assertThat(actual.email).isEqualTo(expected.email)
            }
        }
    }
}