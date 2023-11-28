package com.newspeed.domain.auth.feign

import com.newspeed.domain.auth.application.KakaoOAuth2Service
import com.newspeed.domain.auth.domain.KakaoOAuth2ConfigProperties
import com.newspeed.factory.auth.DummyOAuth2Client
import com.newspeed.global.exception.auth.DuplicateOAuth2Exception
import com.newspeed.global.exception.model.ExceptionType
import com.newspeed.template.UnitTestTemplate
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class OAuth2ClientsTest: UnitTestTemplate {

    @Test
    fun 중복된_타입의_클라이언트가_주어지면_예외가_발생한다() {
        // given
        val oAuth2ClientBuilder = OAuth2Clients.builder()
            .add(DummyOAuth2Client())

        // when & then
        Assertions.assertThatThrownBy { oAuth2ClientBuilder.add(DummyOAuth2Client()) }
            .isInstanceOf(DuplicateOAuth2Exception::class.java)
            .hasMessage(ExceptionType.DUPLICATE_OAUTH2_EXCEPTION.message)
    }

    @Test
    fun 여러_타입의_클라이언트가_주어졌을때_타입으로_찾기를_성공한다() {
        // given
        val dummyOAuth2Client = DummyOAuth2Client()
        val kakaoOAuth2Client = KakaoOAuth2Service(
            tokenClient = Mockito.mock(KakaoOAuth2TokenClient::class.java),
            configProperties = Mockito.mock(KakaoOAuth2ConfigProperties::class.java),
            userClient = Mockito.mock(KakaoOAuth2UserClient::class.java)
        )

        val oAuth2Clients = OAuth2Clients.builder()
            .add(dummyOAuth2Client)
            .add(kakaoOAuth2Client)
            .build()

        // when
        val expect = oAuth2Clients.getClient(com.newspeed.domain.auth.domain.LoginPlatform.KAKAO)

        // then
        assertThat(expect).isExactlyInstanceOf(com.newspeed.domain.auth.application.KakaoOAuth2Service::class.java)
    }
}