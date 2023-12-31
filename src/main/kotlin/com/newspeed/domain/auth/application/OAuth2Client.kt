package com.newspeed.domain.auth.application

import com.newspeed.domain.auth.domain.OAuth2User
import com.newspeed.domain.auth.domain.enums.LoginPlatform

interface OAuth2Client {

    fun getLoginPlatform(): LoginPlatform

    fun getOAuth2User(accessToken: String): OAuth2User
}