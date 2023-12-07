package com.newspeed.domain.auth.application

import com.newspeed.domain.auth.config.AppleOAuth2ConfigProperties
import com.newspeed.domain.auth.domain.OAuth2User
import com.newspeed.domain.auth.domain.enums.LoginPlatform
import com.newspeed.domain.auth.domain.toOAuth2User
import com.newspeed.domain.auth.feign.AppleOAuth2TokenClient
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
        accessToken: String
    ): OAuth2User {
        val tokenRequest = configProperties.toAppleOAuth2TokenRequest(accessToken)
        val token = tokenClient.getOAuthAppleToken(
            tokenRequest.clientId,
            tokenRequest.clientSecret,
            tokenRequest.grantType,
            tokenRequest.authorizationCode
        )

        return jwtAuthExtractor.getClaims(token.idToken)
            .toOAuth2User()
    }
}