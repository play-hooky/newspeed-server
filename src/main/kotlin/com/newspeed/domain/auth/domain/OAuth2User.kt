package com.newspeed.domain.auth.domain

import com.newspeed.domain.auth.domain.enums.LoginPlatform
import com.newspeed.domain.auth.domain.enums.Role
import com.newspeed.domain.user.domain.User
import com.newspeed.global.exception.user.UnavailableEmailException
import io.jsonwebtoken.Claims

data class OAuth2User(
    val platform: LoginPlatform,
    val nickname: String,
    val profileImage: String?,
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

fun Claims.toOAuth2User(): OAuth2User = OAuth2User(
    platform = LoginPlatform.APPLE,
    nickname = this["email"]?.toString()?.split("@")?.get(0) ?: throw UnavailableEmailException(),
    profileImage = null,
    email = this["email"]?.toString() ?: throw UnavailableEmailException()
)