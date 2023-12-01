package com.newspeed.domain.auth.feign.request

data class AppleOAuth2TokenRequest(
    val clientId: String,
    val clientSecret: String,
    val authorizationCode: String,
    val grantType: String
)
