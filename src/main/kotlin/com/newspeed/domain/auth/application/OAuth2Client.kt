package com.newspeed.domain.auth.application

import com.newspeed.domain.auth.domain.OAuth2User
import com.newspeed.domain.auth.domain.enums.LoginPlatform
import com.newspeed.domain.auth.dto.OAuth2UnlinkDTO

interface OAuth2Client {

    fun getLoginPlatform(): LoginPlatform

    fun getOAuth2User(authorizationCode: String): OAuth2User

    fun unlink(oAuth2UnlinkDTO: OAuth2UnlinkDTO)
}