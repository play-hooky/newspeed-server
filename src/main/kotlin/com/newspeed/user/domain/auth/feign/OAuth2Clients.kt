package com.newspeed.user.domain.auth.feign

import com.newspeed.user.domain.auth.application.OAuth2Client
import com.newspeed.user.domain.auth.domain.LoginPlatform
import com.newspeed.user.global.exception.auth.DuplicateOAuth2Exception
import com.newspeed.user.global.exception.auth.UnsupportedOAuth2Exception
import java.util.*

class OAuth2Clients(
    private val oAuth2Clients: Map<LoginPlatform, OAuth2Client>
) {
    companion object {
        fun builder(): OAuth2ClientBuilder = OAuth2ClientBuilder()

        data class OAuth2ClientBuilder(
            val oAuth2Clients: EnumMap<LoginPlatform, OAuth2Client> = EnumMap(LoginPlatform::class.java)
        ) {

            fun addAll(
                oAuth2Clients: List<OAuth2Client>
            ): OAuth2ClientBuilder {
                oAuth2Clients
                    .forEach { add(it) }

                return this
            }

            fun add(
                oAuth2Client: OAuth2Client
            ): OAuth2ClientBuilder {
                oAuth2Clients.computeIfPresent(oAuth2Client.getLoginPlatform()) { _, _ ->
                    throw DuplicateOAuth2Exception()
                }
                oAuth2Clients[oAuth2Client.getLoginPlatform()] = oAuth2Client

                return this
            }

            fun build(): OAuth2Clients = OAuth2Clients(oAuth2Clients)
        }
    }

    fun getClient(
        loginPlatform: LoginPlatform
    ): OAuth2Client = oAuth2Clients[loginPlatform] ?: throw UnsupportedOAuth2Exception()
}