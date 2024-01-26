package com.newspeed.factory.auth

import com.newspeed.domain.auth.application.OAuth2Client
import com.newspeed.domain.auth.domain.OAuth2User
import com.newspeed.domain.auth.domain.enums.LoginPlatform
import org.springframework.stereotype.Service

@Service
class DummyOAuth2Client: OAuth2Client {
    override fun getLoginPlatform(): LoginPlatform = LoginPlatform.NEWSPEED

    override fun getOAuth2User(
        authorizationCode: String
    ): OAuth2User = OAuth2User(
        platform = LoginPlatform.NEWSPEED,
        nickname = "dummy",
        profileImage = "https://www.dummy.com/",
        email = "dummy@dummy.com"
    )

    override fun unlink(
        authorizationCode: String
    ) {

    }
}