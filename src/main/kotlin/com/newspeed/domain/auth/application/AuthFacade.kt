package com.newspeed.domain.auth.application

import com.newspeed.domain.auth.api.request.LoginRequest
import com.newspeed.domain.auth.api.response.LoginResponse
import com.newspeed.domain.auth.api.response.UserResponse
import com.newspeed.domain.auth.application.command.WithdrawalCommand
import com.newspeed.domain.auth.domain.OAuth2User
import com.newspeed.domain.auth.feign.OAuth2Clients
import com.newspeed.domain.jwt.application.JwtService
import com.newspeed.domain.user.application.UserService
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import javax.validation.Valid

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

    fun logout(
        userId: Long
    ) {
        jwtService.deleteRefreshToken(userId)
    }

    private fun getOAuth2UserFrom(
        loginRequest: LoginRequest
    ): OAuth2User = oAuth2Clients.getClient(loginRequest.loginPlatform)
        .getOAuth2User(loginRequest.authorizationCode)

    fun getUserResponse(
        userId: Long
    ): UserResponse = userService.getUser(userId)
        .toResponse()

    fun reissueAccessToken(
        userId: Long
    ): String {
        val authPayload = userService.getUser(userId).toAuthPayload()

        return jwtService.reissueAccessToken(authPayload)
    }

    @Transactional
    fun unlink(
        @Valid command: WithdrawalCommand
    ) {
        val user = userService.getUser(command.userId)
            .also { it.delete() }
        jwtService.deleteRefreshToken(user.id)

        oAuth2Clients.getClient(user.platform)
            .unlink(command.authorizationCode)
    }
}