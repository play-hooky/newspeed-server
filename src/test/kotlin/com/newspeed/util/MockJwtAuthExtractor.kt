package com.newspeed.util

import com.newspeed.domain.auth.domain.AuthPayload
import com.newspeed.domain.auth.domain.AuthenticateContext
import com.newspeed.domain.auth.domain.enums.LoginPlatform
import com.newspeed.domain.jwt.application.JwtExtractor
import com.newspeed.factory.auth.AuthFactory.Companion.DUMMY_EMAIL
import com.newspeed.global.exception.auth.IllegalAuthorizationException
import com.newspeed.global.exception.auth.InvalidJwtException
import com.newspeed.global.exception.model.InternalServerException
import io.jsonwebtoken.*
import io.jsonwebtoken.security.SignatureException

class MockJwtAuthExtractor(
    private val jwtParser: JwtParser,
    private val authenticateContext: AuthenticateContext
): JwtExtractor {
    override fun extract(
        token: String
    ): AuthPayload = AuthPayload(
        authenticateContext.userId(),
        authenticateContext.role(),
        LoginPlatform.NEWSPEED,
        DUMMY_EMAIL
    )

    override fun getClaims(
        jwt: String
    ): Claims {
        try {
            return jwtParser.parseClaimsJws(jwt).body
        } catch (exception: Exception) {
            when (exception) {
                is ExpiredJwtException -> throw com.newspeed.global.exception.auth.ExpiredJwtException()
                is SignatureException -> throw InvalidJwtException()
                is IllegalArgumentException -> throw InvalidJwtException()
                is MalformedJwtException -> throw InvalidJwtException()
                is UnsupportedJwtException -> throw IllegalAuthorizationException()
                else -> throw InternalServerException()
            }
        }
    }
}