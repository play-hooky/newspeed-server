package com.newspeed.domain.auth.application

import com.newspeed.domain.auth.config.AppleOAuth2ConfigProperties
import com.newspeed.domain.auth.domain.OAuth2User
import com.newspeed.domain.auth.domain.enums.LoginPlatform
import com.newspeed.domain.auth.domain.toOAuth2User
import com.newspeed.domain.auth.dto.OAuth2UnlinkDTO
import com.newspeed.domain.auth.feign.AppleOAuth2TokenClient
import com.newspeed.domain.auth.feign.enums.AppleTokenTypeHint
import com.newspeed.domain.auth.feign.request.AppleOAuth2TokenRequest
import com.newspeed.domain.auth.feign.response.AppleOAuth2TokenResponse
import com.newspeed.domain.jwt.application.AppleJwtAuthExtractor
import org.springframework.stereotype.Service


@Service
class AppleOAuth2Service(
    val configProperties: AppleOAuth2ConfigProperties,
    val tokenClient: AppleOAuth2TokenClient,
    val jwtAuthExtractor: AppleJwtAuthExtractor
): OAuth2Client {
    override fun getLoginPlatform(): LoginPlatform = LoginPlatform.APPLE

    override fun getOAuth2User(
        authorizationCode: String
    ): OAuth2User {
        val token = getToken(authorizationCode)

        return jwtAuthExtractor.getClaims(token.idToken)
            .toOAuth2User()
    }

    private fun getOAuth2User(
        token: AppleOAuth2TokenResponse
    ): OAuth2User = jwtAuthExtractor.getClaims(token.idToken)
        .toOAuth2User()

    override fun unlink(
        oAuth2UnlinkDTO: OAuth2UnlinkDTO
    ) {
        val tokenRequest = getTokenRequest(oAuth2UnlinkDTO.authorizationCode)
        val token = getToken(tokenRequest)

        oAuth2UnlinkDTO.validateByEmail(getOAuth2User(token).email)
        revokeToken(tokenRequest, token.accessToken)
    }

    private fun revokeToken(
        tokenRequest: AppleOAuth2TokenRequest,
        accessToken: String
    ) {
        tokenClient.revoke(
            tokenRequest.clientId,
            tokenRequest.clientSecret,
            accessToken,
            AppleTokenTypeHint.ACCESS_TOKEN.value
        )
    }

    private fun getToken(
        authorizationCode: String
    ): AppleOAuth2TokenResponse {
        val tokenRequest = getTokenRequest(authorizationCode)

        return tokenClient.getOAuthAppleToken(
            tokenRequest.clientId,
            tokenRequest.clientSecret,
            tokenRequest.grantType,
            tokenRequest.authorizationCode
        )
    }

    private fun getToken(
        tokenRequest: AppleOAuth2TokenRequest
    ): AppleOAuth2TokenResponse = tokenClient.getOAuthAppleToken(
        tokenRequest.clientId,
        tokenRequest.clientSecret,
        tokenRequest.grantType,
        tokenRequest.authorizationCode
    )

    private fun getTokenRequest(
        authorizationCode: String
    ): AppleOAuth2TokenRequest = configProperties.toAppleOAuth2TokenRequest(authorizationCode)
}