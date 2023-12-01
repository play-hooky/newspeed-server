package com.newspeed.domain.auth.domain

import com.newspeed.domain.auth.feign.request.KakaoOAuth2TokenRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
data class KakaoOAuth2ConfigProperties(
    @Value("\${oauth.kakao.rest-api-key}") val key: String,
    @Value("\${oauth.grant-type}") val grantType: String,
    @Value("\${oauth.kakao.redirect-uri}") val redirectUri: String,
) {
    fun toKakaoOAuth2TokenRequest(
        authorizationCode: String
    ): KakaoOAuth2TokenRequest = KakaoOAuth2TokenRequest(
        grantType = grantType,
        clientId =  key,
        redirectUri = redirectUri,
        authorizationCode = authorizationCode
    )
}