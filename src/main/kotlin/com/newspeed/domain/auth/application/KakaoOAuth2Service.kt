package com.newspeed.domain.auth.application

import com.newspeed.domain.auth.feign.KakaoOAuth2TokenClient
import com.newspeed.domain.auth.feign.KakaoOAuth2UserClient
import org.springframework.stereotype.Service

@Service
class KakaoOAuth2Service(
    private val configProperties: com.newspeed.domain.auth.domain.KakaoOAuth2ConfigProperties,
    private val tokenClient: KakaoOAuth2TokenClient,
    private val userClient: KakaoOAuth2UserClient
): com.newspeed.domain.auth.application.OAuth2Client {
    override fun getLoginPlatform(): com.newspeed.domain.auth.domain.LoginPlatform = com.newspeed.domain.auth.domain.LoginPlatform.KAKAO

    override fun getOAuth2User(
        accessToken: String
    ): com.newspeed.domain.auth.domain.OAuth2User {
        val tokenRequest = configProperties.toKakaoOAuth2TokenRequest(accessToken)
        val token = tokenClient.getOAuthKakaoToken(tokenRequest)

        return userClient.getKakaoUserEntity(token.getUserEntityRequestHeader())
            .toOAuth2User()
    }
}