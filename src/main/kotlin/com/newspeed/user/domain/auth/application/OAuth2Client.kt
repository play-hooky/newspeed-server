package com.newspeed.user.domain.auth.application

import com.newspeed.user.domain.auth.domain.OAuth2User

interface OAuth2Client {

    fun getOAuth2User(accessToken: String): OAuth2User
}