package com.newspeed.domain.auth.config

import com.newspeed.domain.auth.application.AuthInterceptor
import com.newspeed.domain.auth.domain.AuthenticateContext
import com.newspeed.domain.auth.domain.enums.Role
import com.newspeed.domain.jwt.application.HeaderTokenExtractor
import com.newspeed.domain.jwt.application.JwtAuthExtractor
import com.newspeed.domain.user.application.RoleArgumentResolver
import io.jsonwebtoken.JwtParser
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.nio.charset.StandardCharsets
import javax.crypto.SecretKey

@Configuration
class LoginConfig(
    private val authenticateContext: AuthenticateContext,
    @Value("\${jwt.client-secret}") val secretKey: String
): WebMvcConfigurer {
    override fun addArgumentResolvers(
        resolvers: MutableList<HandlerMethodArgumentResolver>
    ) {
        resolvers.add(RoleArgumentResolver(Role.USER, authenticateContext))
    }

    override fun addInterceptors(
        registry: InterceptorRegistry
    ) {
        registry.addInterceptor(
            authInterceptor(jwtAuthExtractor(jwtParser(secretKey()))))
            .addPathPatterns("/**")
            .excludePathPatterns(
                "/login",
                "/user/login",
                "/favicon.ico",
                "/contents/query/recommend"
            )
    }

    @Bean
    fun authInterceptor(
        jwtAuthExtractor: JwtAuthExtractor
    ): AuthInterceptor = AuthInterceptor(
        authenticateContext = authenticateContext,
        tokenExtractor = HeaderTokenExtractor(),
        jwtAuthExtractor = jwtAuthExtractor
    )

    @Bean
    fun jwtAuthExtractor(
        jwtParser: JwtParser
    ): JwtAuthExtractor = JwtAuthExtractor(jwtParser)

    @Bean
    fun jwtParser(
        secretKey: SecretKey
    ): JwtParser = Jwts.parserBuilder()
        .setSigningKey(secretKey)
        .build()

    @Bean
    fun secretKey(): SecretKey = Keys.hmacShaKeyFor(secretKey.toByteArray(StandardCharsets.UTF_8))
}