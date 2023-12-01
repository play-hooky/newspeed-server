package com.newspeed.domain.auth.application

import com.newspeed.domain.auth.api.request.LoginRequest
import com.newspeed.domain.auth.api.response.LoginResponse
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
    fun login(
        loginRequest: LoginRequest
    ): LoginResponse {
        val oAuth2User = getOAuth2UserFrom(loginRequest)
        val user = userService.saveIfNotExist(oAuth2User)

        val authPayload = user.toAuthPayload()
        val jwts = jwtService.issueAllJwt(authPayload)

        jwtService.saveRefreshToken(user.id, jwts.refreshToken)

        return LoginResponse(
            accessToken = jwts.accessToken,
            refreshToken = jwts.refreshToken,
            userId = user.id
        )
    }

    private fun getOAuth2UserFrom(
        loginRequest: LoginRequest
    ): OAuth2User = oAuth2Clients.getClient(loginRequest.loginPlatform)
        .getOAuth2User(loginRequest.authorizationCode)
}