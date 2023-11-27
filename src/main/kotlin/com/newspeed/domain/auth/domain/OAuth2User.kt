package com.newspeed.domain.auth.domain

import com.newspeed.domain.user.domain.User

data class OAuth2User(
    val platform: LoginPlatform,
    val nickname: String,
    val profileImage: String,
    val email: String
) {

    fun toUser(): User = User(
        email = email,
        nickname = nickname,
        platform = platform,
        profileImageUrl = profileImage,
        role = Role.USER
    )
}
