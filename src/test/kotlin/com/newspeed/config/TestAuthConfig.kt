package com.newspeed.config

import com.newspeed.domain.auth.domain.AuthenticateContext
import com.newspeed.domain.auth.domain.enums.Role
import com.newspeed.domain.user.application.RoleArgumentResolver
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

@TestConfiguration
class TestAuthConfig {

    @Bean
    fun roleArgumentResolver(
        authenticateContext: AuthenticateContext
    ): RoleArgumentResolver = RoleArgumentResolver(
        role = Role.USER,
        authenticateContext = authenticateContext
    )
}