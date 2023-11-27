package com.newspeed.domain.jwt.dto

data class IssuedJwtDTO(
    val accessToken: String,
    val refreshToken: String
)
