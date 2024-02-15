package com.newspeed.config

import com.newspeed.domain.auth.domain.AuthenticateContext
import com.newspeed.domain.auth.domain.enums.Role
import com.newspeed.domain.jwt.application.HeaderTokenExtractor
import com.newspeed.domain.jwt.application.JwtAuthExtractor
import com.newspeed.domain.jwt.application.JwtExtractor
import com.newspeed.domain.user.application.RoleArgumentResolver
import com.newspeed.util.MockJwtAuthExtractor
import io.jsonwebtoken.JwtParser
import org.mockito.Mockito
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary

@TestConfiguration
class TestAuthConfig {

    @Bean
    fun authenticateContext(): AuthenticateContext = AuthenticateContext()

    @Bean
    fun roleArgumentResolver(
        authenticateContext: AuthenticateContext
    ): RoleArgumentResolver = RoleArgumentResolver(
        role = Role.USER,
        authenticateContext = authenticateContext
    )

    @Bean
    @Primary
    fun headerTokenExtractor(): HeaderTokenExtractor = HeaderTokenExtractor()

    @Bean
    fun jwtAuthExtractor(
        authenticateContext: AuthenticateContext
    ): JwtExtractor = MockJwtAuthExtractor(
        Mockito.spy(JwtParser::class.java),
        authenticateContext
    )
}