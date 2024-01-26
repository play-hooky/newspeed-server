package com.newspeed.domain.auth.application

import com.newspeed.domain.auth.config.KakaoOAuth2ConfigProperties
import com.newspeed.domain.auth.domain.OAuth2User
import com.newspeed.domain.auth.domain.enums.LoginPlatform
import com.newspeed.domain.auth.dto.OAuth2UnlinkDTO
import com.newspeed.domain.auth.feign.KakaoOAuth2TokenClient
import com.newspeed.domain.auth.feign.KakaoOAuth2UserClient
import com.newspeed.domain.auth.feign.response.KakaoOAuth2TokenResponse
import org.springframework.stereotype.Service

@Service
class KakaoOAuth2Service(
    private val tokenClient: KakaoOAuth2TokenClient,
    private val configProperties: KakaoOAuth2ConfigProperties,
    private val userClient: KakaoOAuth2UserClient
): OAuth2Client {
    override fun getLoginPlatform(): LoginPlatform = LoginPlatform.KAKAO

    override fun getOAuth2User(
        authorizationCode: String
    ): OAuth2User {
        val token = getToken(authorizationCode)

        return getOAuth2User(token)
    }

    private fun getOAuth2User(
        token: KakaoOAuth2TokenResponse
    ): OAuth2User = userClient.getKakaoUserEntity(token.getAuthorizationHeader())
        .toOAuth2User()

    override fun unlink(
        oAuth2UnlinkDTO: OAuth2UnlinkDTO
    ) {
        val token = getToken(oAuth2UnlinkDTO.authorizationCode)

        oAuth2UnlinkDTO.validateByEmail(getOAuth2User(token).email)
        userClient.unlink(token.getAuthorizationHeader())
    }

    private fun getToken(
        authorizationCode: String
    ): KakaoOAuth2TokenResponse {
        val tokenRequest = configProperties.toKakaoOAuth2TokenRequest(authorizationCode)

        return tokenClient.getOAuthKakaoToken(tokenRequest)
    }
}