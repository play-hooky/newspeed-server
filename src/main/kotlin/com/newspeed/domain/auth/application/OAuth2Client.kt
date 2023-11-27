package com.newspeed.domain.auth.application

interface OAuth2Client {

    fun getLoginPlatform(): com.newspeed.domain.auth.domain.LoginPlatform

    fun getOAuth2User(accessToken: String): com.newspeed.domain.auth.domain.OAuth2User
}