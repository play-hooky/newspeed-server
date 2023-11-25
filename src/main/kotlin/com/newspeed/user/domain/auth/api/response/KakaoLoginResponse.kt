package com.newspeed.user.domain.auth.api.response

data class KakaoLoginResponse(
    val refreshToken: String,
    val accessToken: String,
    val userId: Long
)