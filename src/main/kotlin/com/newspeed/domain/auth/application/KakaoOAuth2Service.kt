package com.newspeed.domain.auth.application

import com.newspeed.domain.auth.domain.KakaoOAuth2ConfigProperties
import com.newspeed.domain.auth.domain.LoginPlatform
import com.newspeed.domain.auth.domain.OAuth2User
import com.newspeed.domain.auth.feign.KakaoOAuth2TokenClient
import com.newspeed.domain.auth.feign.KakaoOAuth2UserClient
import org.springframework.stereotype.Service

@Service
class KakaoOAuth2Service(
    private val tokenClient: KakaoOAuth2TokenClient,
    private val configProperties: KakaoOAuth2ConfigProperties,
    private val userClient: KakaoOAuth2UserClient
): OAuth2Client {
    override fun getLoginPlatform(): LoginPlatform = LoginPlatform.KAKAO

    override fun getOAuth2User(
        accessToken: String
    ): OAuth2User {
        val tokenRequest = configProperties.toKakaoOAuth2TokenRequest(accessToken)
        val token = tokenClient.getOAuthKakaoToken(tokenRequest)

        return userClient.getKakaoUserEntity(token.getUserEntityRequestHeader())
            .toOAuth2User()
    }
}