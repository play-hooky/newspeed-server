package com.newspeed.domain.jwt.domain

import io.jsonwebtoken.JwtParser
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import javax.crypto.SecretKey

@Component
data class JwtConfigProperties(
    @Value("\${jwt.header}") val header: String,
    @Value("\${jwt.issuer}") val issuer: String,
    @Value("\${jwt.client-secret}") val secretKey: String,
    @Value("\${jwt.refresh-token-expiry-seconds}") val refreshTokenExpirySeconds: Long,
    @Value("\${jwt.access-token-expiry-seconds}") val accessTokenExpirySeconds: Long
) {
    @Bean
    fun secretKey(): SecretKey = Keys.hmacShaKeyFor(secretKey.toByteArray(StandardCharsets.UTF_8))

    @Bean
    fun jwtParser(
        secretKey: SecretKey
    ): JwtParser = Jwts.parserBuilder()
        .setSigningKey(secretKey)
        .build()
}
