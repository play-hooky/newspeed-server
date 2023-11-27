package com.newspeed.domain.auth.domain

data class OAuth2User(
    val platform: com.newspeed.domain.auth.domain.LoginPlatform,
    val nickname: String,
    val profileImage: String,
    val email: String
) {
    fun toAuthPayload(
        userId: Long
    ): com.newspeed.domain.auth.domain.AuthPayload = com.newspeed.domain.auth.domain.AuthPayload(
        userId = userId,
        role = com.newspeed.domain.auth.domain.Role.USER,
        loginPlatform = platform,
        email = email
    )
}
