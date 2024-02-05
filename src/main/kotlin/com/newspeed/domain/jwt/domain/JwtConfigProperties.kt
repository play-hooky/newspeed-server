package com.newspeed.domain.jwt.domain

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
data class JwtConfigProperties(
    @Value("\${jwt.header}") val header: String,
    @Value("\${jwt.issuer}") val issuer: String,
    @Value("\${jwt.client-secret}") val secretKey: String,
    @Value("\${jwt.refresh-token-expiry-seconds}") val refreshTokenExpirySeconds: Long,
    @Value("\${jwt.access-token-expiry-seconds}") val accessTokenExpirySeconds: Long
)
