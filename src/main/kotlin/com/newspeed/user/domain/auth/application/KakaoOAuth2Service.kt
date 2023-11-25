package com.newspeed.user.domain.auth.application

import com.newspeed.user.domain.auth.domain.KakaoOAuth2ConfigProperties
import com.newspeed.user.domain.auth.domain.LoginPlatform
import com.newspeed.user.domain.auth.domain.OAuth2User
import com.newspeed.user.domain.auth.feign.KakaoOAuth2TokenClient
import com.newspeed.user.domain.auth.feign.KakaoOAuth2UserClient
import org.springframework.stereotype.Service

@Service
class KakaoOAuth2Service(
    private val configProperties: KakaoOAuth2ConfigProperties,
    private val tokenClient: KakaoOAuth2TokenClient,
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