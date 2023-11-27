package com.newspeed.factory.auth

import com.newspeed.domain.auth.application.OAuth2Client
import com.newspeed.domain.auth.domain.LoginPlatform
import com.newspeed.domain.auth.domain.OAuth2User

class DummyOAuth2Client: com.newspeed.domain.auth.application.OAuth2Client {
    override fun getLoginPlatform(): com.newspeed.domain.auth.domain.LoginPlatform = com.newspeed.domain.auth.domain.LoginPlatform.APPLE

    override fun getOAuth2User(
        accessToken: String
    ): com.newspeed.domain.auth.domain.OAuth2User = com.newspeed.domain.auth.domain.OAuth2User(
        platform = com.newspeed.domain.auth.domain.LoginPlatform.APPLE,
        nickname = "dummy",
        profileImage = "https://www.dummy.com/",
        email = "dummy@dummy.com"
    )
}