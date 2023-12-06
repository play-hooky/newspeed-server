package com.newspeed.domain.auth.domain.enums

enum class LoginPlatform {
    KAKAO,
    APPLE,
    NEWSPEED;

    companion object {
        fun from(
            loginPlatform: String
        ): LoginPlatform = LoginPlatform.valueOf(loginPlatform)
    }
}