package com.newspeed.user.factory.auth

import com.newspeed.user.domain.auth.application.OAuth2Client
import com.newspeed.user.domain.auth.domain.LoginPlatform
import com.newspeed.user.domain.auth.domain.OAuth2User

class DummyOAuth2Client: OAuth2Client {
    override fun getLoginPlatform(): LoginPlatform = LoginPlatform.APPLE

    override fun getOAuth2User(
        accessToken: String
    ): OAuth2User = OAuth2User(
        platform = LoginPlatform.APPLE,
        nickname = "dummy",
        profileImage = "https://www.dummy.com/",
        email = "dummy@dummy.com"
    )
}