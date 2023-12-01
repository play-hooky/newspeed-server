package com.newspeed.domain.jwt.application

import com.newspeed.domain.auth.domain.AuthPayload
import com.newspeed.domain.jwt.domain.JwtConfigProperties
import com.newspeed.domain.jwt.dto.IssuedJwtDTO
import io.jsonwebtoken.JwtBuilder
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtAuthProvider(
    private val jwtConfigProperties: JwtConfigProperties,
    private val secretKey: SecretKey
) {

    fun issueAllJwt(
        authPayload: AuthPayload
    ): IssuedJwtDTO = IssuedJwtDTO(
        accessToken = provideAccessToken(authPayload),
        refreshToken = provideRefreshToken(authPayload)
    )

    fun provideRefreshToken(
        authPayload: AuthPayload
    ): String {
        val now = Date()

        return authPayload
            .toJwtClaims()
            .build(now, jwtConfigProperties.refreshTokenExpirySeconds, secretKey)
    }

    fun provideAccessToken(
        authPayload: AuthPayload
    ): String {
        val now = Date()

        return authPayload
            .toJwtClaims()
            .build(now, jwtConfigProperties.accessTokenExpirySeconds, secretKey)
    }
}

private fun JwtBuilder.build(
    now: Date,
    expiration: Long,
    key: SecretKey
): String = this
    .setIssuedAt(now)
    .setExpiration(Date(now.time + expiration * 1000))
    .signWith(key)
    .compact()