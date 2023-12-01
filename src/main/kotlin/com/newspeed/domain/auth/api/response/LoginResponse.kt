package com.newspeed.domain.auth.api.response

data class LoginResponse(
    val refreshToken: String,
    val accessToken: String,
    val userId: Long
)