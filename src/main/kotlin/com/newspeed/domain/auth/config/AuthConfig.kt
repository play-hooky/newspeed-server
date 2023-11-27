package com.newspeed.domain.auth.config

import com.newspeed.domain.auth.application.OAuth2Client
import com.newspeed.domain.auth.feign.OAuth2Clients
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AuthConfig {

    @Bean
    fun oAuth2Clients(
        oAuth2Clients: List<OAuth2Client>
    ): OAuth2Clients = OAuth2Clients.builder()
        .addAll(oAuth2Clients)
        .build()
}