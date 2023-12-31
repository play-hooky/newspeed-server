package com.newspeed.domain.auth.domain

import com.newspeed.domain.auth.domain.AuthPayload.Companion.EMAIL_KEY
import com.newspeed.domain.auth.domain.AuthPayload.Companion.PLATFORM_KEY
import com.newspeed.domain.auth.domain.AuthPayload.Companion.ROLE_KEY
import com.newspeed.domain.auth.domain.AuthPayload.Companion.USER_ID_KEY
import com.newspeed.domain.auth.domain.enums.LoginPlatform
import com.newspeed.domain.auth.domain.enums.Role
import com.newspeed.global.exception.auth.InsufficientJwtClaimException
import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtBuilder
import io.jsonwebtoken.Jwts

data class AuthPayload(
    val userId: Long,
    val role: Role,
    val loginPlatform: LoginPlatform,
    val email: String
) {
    companion object {
        const val USER_ID_KEY = "userId"
        const val ROLE_KEY = "role"
        const val PLATFORM_KEY = "platform"
        const val EMAIL_KEY = "email"
    }

    fun toJwtClaims(): JwtBuilder =  Jwts.builder()
        .claim(USER_ID_KEY, userId)
        .claim(ROLE_KEY, role)
        .claim(PLATFORM_KEY, loginPlatform)
        .claim(EMAIL_KEY, email)
}

fun Claims.toAuthPayload(): AuthPayload = AuthPayload(
    userId = this[USER_ID_KEY]?.toString()?.toLong() ?: throw InsufficientJwtClaimException("userId"),
    role = Role.from(this[ROLE_KEY]?.toString() ?: throw InsufficientJwtClaimException("role")),
    loginPlatform = LoginPlatform.from(this[PLATFORM_KEY]?.toString() ?: throw InsufficientJwtClaimException("loginPlatform")),
    email = this[EMAIL_KEY]?.toString() ?: throw InsufficientJwtClaimException("email")
)