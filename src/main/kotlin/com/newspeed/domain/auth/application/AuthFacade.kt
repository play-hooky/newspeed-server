package com.newspeed.domain.auth.application

import com.newspeed.domain.auth.api.request.KakaoLoginRequest
import com.newspeed.domain.auth.api.response.KakaoLoginResponse
import com.newspeed.domain.auth.domain.LoginPlatform
import com.newspeed.domain.auth.domain.OAuth2User
import com.newspeed.domain.auth.feign.OAuth2Clients
import com.newspeed.domain.jwt.application.JwtService
import com.newspeed.domain.user.application.UserService
import org.springframework.stereotype.Component

@Component
class AuthFacade(
    private val oAuth2Clients: OAuth2Clients,
    private val jwtService: JwtService,
    private val userService: UserService
) {
    fun kakaoLogin(
        kakaoLoginRequest: KakaoLoginRequest
    ): KakaoLoginResponse {
        val oAuth2User = getKakaoUser(kakaoLoginRequest)
        val user = userService.saveIfNotExist(oAuth2User)

        val authPayload = user.toAuthPayload()
        val jwts = jwtService.issueAllJwt(authPayload)

        jwtService.saveRefreshToken(user.id, jwts.refreshToken)

        return KakaoLoginResponse(
            accessToken = jwts.accessToken,
            refreshToken = jwts.refreshToken,
            userId = user.id
        )
    }

    private fun getKakaoUser(
        kakaoLoginRequest: KakaoLoginRequest
    ): OAuth2User = oAuth2Clients.getClient(LoginPlatform.KAKAO)
        .getOAuth2User(kakaoLoginRequest.authorizationCode)
}