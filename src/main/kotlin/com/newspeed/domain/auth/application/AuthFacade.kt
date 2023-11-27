package com.newspeed.domain.auth.application

import com.newspeed.domain.auth.feign.OAuth2Clients
import com.newspeed.domain.jwt.application.JwtAuthProvider
import org.springframework.stereotype.Component

@Component
class AuthFacade(
    private val oAuth2Clients: OAuth2Clients,
    private val jwtAuthProvider: JwtAuthProvider
) {

    fun kakaoLogin(
        kakaoLoginRequest: com.newspeed.domain.auth.api.request.KakaoLoginRequest
    ): com.newspeed.domain.auth.api.response.KakaoLoginResponse {
        val oAuth2User = getKakaoUser(kakaoLoginRequest)

        // todo: user entity 회원가입 및 로그인
        val userId = 1L

        val accessToken = jwtAuthProvider.provideAccessToken(oAuth2User.toAuthPayload(userId))
        val refreshToken = jwtAuthProvider.provideRefreshToken(oAuth2User.toAuthPayload(userId))

        return com.newspeed.domain.auth.api.response.KakaoLoginResponse(
            accessToken = accessToken,
            refreshToken = refreshToken,
            userId = userId
        )
    }

    private fun getKakaoUser(
        kakaoLoginRequest: com.newspeed.domain.auth.api.request.KakaoLoginRequest
    ): com.newspeed.domain.auth.domain.OAuth2User = oAuth2Clients.getClient(com.newspeed.domain.auth.domain.LoginPlatform.KAKAO)
        .getOAuth2User(kakaoLoginRequest.authorizationCode)
}