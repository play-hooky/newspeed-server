package com.newspeed.user.domain.auth.application

import com.newspeed.user.domain.auth.domain.AuthPayload
import com.newspeed.user.domain.auth.domain.JwtConfigProperties
import io.jsonwebtoken.JwtBuilder
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtAuthProvider(
    private val jwtConfigProperties: JwtConfigProperties
) {

    fun provideRefreshToken(
        authPayload: AuthPayload
    ): String {
        val now = Date()

        return authPayload
            .toJwtClaims()
            .build(now, jwtConfigProperties.refreshTokenExpirySeconds, jwtConfigProperties.secretKey)
    }

    fun provideAccessToken(
        authPayload: AuthPayload
    ): String {
        val now = Date()

        return authPayload
            .toJwtClaims()
            .build(now, jwtConfigProperties.accessTokenExpirySeconds, jwtConfigProperties.secretKey)
    }
}

private fun JwtBuilder.build(
    now: Date,
    expiration: Long,
    key: String
): String = this
    .setIssuedAt(now)
    .setExpiration(Date(now.time + expiration * 1000))
    .signWith(SignatureAlgorithm.HS256, key)
    .compact()