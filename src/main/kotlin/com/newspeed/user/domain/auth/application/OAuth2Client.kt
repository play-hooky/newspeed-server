package com.newspeed.user.domain.auth.application

import com.newspeed.user.domain.auth.domain.LoginPlatform
import com.newspeed.user.domain.auth.domain.OAuth2User

interface OAuth2Client {

    fun getLoginPlatform(): LoginPlatform

    fun getOAuth2User(accessToken: String): OAuth2User
}