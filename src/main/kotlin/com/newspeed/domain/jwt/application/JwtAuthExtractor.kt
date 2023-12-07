package com.newspeed.domain.jwt.application

import com.newspeed.domain.auth.domain.AuthPayload
import com.newspeed.domain.auth.domain.toAuthPayload
import com.newspeed.global.exception.auth.IllegalAuthorizationException
import com.newspeed.global.exception.auth.InvalidJwtException
import com.newspeed.global.exception.model.InternalServerException
import io.jsonwebtoken.*
import io.jsonwebtoken.security.SignatureException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class JwtAuthExtractor(
    private val jwtParser: JwtParser
): JwtExtractor {
    companion object {
        private val log = LoggerFactory.getLogger(JwtAuthExtractor::class.java)
    }

    override fun extract(
        token: String
    ): AuthPayload = getClaims(token)
        .toAuthPayload()

    override fun getClaims(
        jwt: String
    ): Claims {
        try {
            return jwtParser.parseClaimsJws(jwt).body
        } catch (exception: Exception) {
            log.error(exception.stackTraceToString())

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