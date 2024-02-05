package com.newspeed.factory.global

import com.newspeed.domain.auth.domain.AuthenticateContext
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

@TestConfiguration
class TestAuthConfig {

    @Bean
    fun authenticateContext(): AuthenticateContext = AuthenticateContext()
}