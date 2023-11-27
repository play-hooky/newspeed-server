package com.newspeed.domain.auth.feign

import com.newspeed.global.exception.auth.DuplicateOAuth2Exception
import com.newspeed.global.exception.auth.UnsupportedOAuth2Exception
import java.util.*

class OAuth2Clients(
    private val oAuth2Clients: Map<com.newspeed.domain.auth.domain.LoginPlatform, com.newspeed.domain.auth.application.OAuth2Client>
) {
    companion object {
        fun builder(): OAuth2ClientBuilder = OAuth2ClientBuilder()

        data class OAuth2ClientBuilder(
            val oAuth2Clients: EnumMap<com.newspeed.domain.auth.domain.LoginPlatform, com.newspeed.domain.auth.application.OAuth2Client> = EnumMap(
                com.newspeed.domain.auth.domain.LoginPlatform::class.java)
        ) {

            fun addAll(
                oAuth2Clients: List<com.newspeed.domain.auth.application.OAuth2Client>
            ): OAuth2ClientBuilder {
                oAuth2Clients
                    .forEach { add(it) }

                return this
            }

            fun add(
                oAuth2Client: com.newspeed.domain.auth.application.OAuth2Client
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
        loginPlatform: com.newspeed.domain.auth.domain.LoginPlatform
    ): com.newspeed.domain.auth.application.OAuth2Client = oAuth2Clients[loginPlatform] ?: throw UnsupportedOAuth2Exception()
}