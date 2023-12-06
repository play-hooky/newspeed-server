package com.newspeed.domain.auth.config

import com.newspeed.domain.auth.application.OAuth2Client
import com.newspeed.domain.auth.domain.AuthenticateContext
import com.newspeed.domain.auth.domain.enums.Role
import com.newspeed.domain.auth.feign.OAuth2Clients
import com.newspeed.domain.user.application.RoleArgumentResolver
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AuthConfig(
    private val authenticateContext: AuthenticateContext
) {

    @Bean
    fun oAuth2Clients(
        oAuth2Clients: List<OAuth2Client>
    ): OAuth2Clients = OAuth2Clients.builder()
        .addAll(oAuth2Clients)
        .build()

    @Bean
    fun roleArgumentResolver(): RoleArgumentResolver = RoleArgumentResolver(
        role = Role.USER,
        authenticateContext = authenticateContext
    )
}