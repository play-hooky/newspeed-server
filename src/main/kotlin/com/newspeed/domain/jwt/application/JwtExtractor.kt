package com.newspeed.domain.jwt.application

import com.newspeed.domain.auth.domain.AuthPayload
import io.jsonwebtoken.Claims

interface JwtExtractor {
    fun extract(token: String): AuthPayload

    fun getClaims(jwt: String): Claims
}