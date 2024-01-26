package com.newspeed.domain.auth.feign.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
@JsonIgnoreProperties(ignoreUnknown = true)
data class KakaoOAuth2TokenResponse(
    val tokenType: String,
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Int,
    val scope: String,
    val refreshTokenExpiresIn: Int
) {
    fun getAuthorizationHeader(): String = "Bearer $accessToken"
}
