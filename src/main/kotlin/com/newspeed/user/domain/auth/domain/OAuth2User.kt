package com.newspeed.user.domain.auth.domain

data class OAuth2User(
    val platform: LoginPlatform,
    val nickname: String,
    val profileImage: String,
    val email: String
)
