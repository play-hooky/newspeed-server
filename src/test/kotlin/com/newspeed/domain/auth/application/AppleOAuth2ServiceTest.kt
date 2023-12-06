package com.newspeed.domain.auth.application

import com.newspeed.domain.auth.config.AppleOAuth2ConfigProperties
import com.newspeed.domain.auth.feign.AppleOAuth2TokenClient
import com.newspeed.template.IntegrationTestTemplate
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class AppleOAuth2ServiceTest: IntegrationTestTemplate {
    @Autowired
    private lateinit var tokenConfigProperties: AppleOAuth2ConfigProperties

    @Autowired
    private lateinit var appleOAuth2TokenClient: AppleOAuth2TokenClient

    @Autowired
    private lateinit var appleOAuth2Service: AppleOAuth2Service

    @Test
    fun test() {
        val authorizationCode = "c59359578477e48d0ba858bc920ec85e0.0.rszx.G2RV9MKofvddS2KxNO0P4g"
        val request = tokenConfigProperties.toAppleOAuth2TokenRequest(authorizationCode)

        val token = appleOAuth2TokenClient.getOAuthAppleToken(
            request.clientId,
            request.clientSecret,
            request.grantType,
            request.authorizationCode
        )
        println("$token\n\n")


        val claims = appleOAuth2Service.parseClaims(token.idToken.lines()
            .filter { line -> !line.startsWith("--") }
            .joinToString("")
            .replace("\\s".toRegex(), ""))

        println("$claims\n\n")
    }
}