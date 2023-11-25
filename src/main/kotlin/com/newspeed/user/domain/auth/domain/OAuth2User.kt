package com.newspeed.user.domain.auth.domain

data class OAuth2User(
    val platform: LoginPlatform,
    val nickname: String,
    val profileImage: String,
    val email: String
) {
    fun toAuthPayload(
        userId: Long
    ): AuthPayload = AuthPayload(
        userId = userId,
        role = Role.USER,
        loginPlatform = platform,
        email = email
    )
}
