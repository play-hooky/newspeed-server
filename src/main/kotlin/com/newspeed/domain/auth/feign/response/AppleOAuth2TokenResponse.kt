package com.newspeed.domain.auth.feign.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
@JsonIgnoreProperties(ignoreUnknown = true)
data class AppleOAuth2TokenResponse(
    val tokenType: String,
    val accessToken: String,
    val expiresIn: Int,
    val idToken: String
)
