package com.newspeed.user.domain.auth.domain

import io.jsonwebtoken.JwtBuilder
import io.jsonwebtoken.Jwts

data class AuthPayload(
    val userId: Long,
    val role: Role,
    val loginPlatform: LoginPlatform,
    val email: String
) {
    companion object {
        private const val USER_ID_KEY = "userId"
        private const val ROLE_KEY = "role"
        private const val PLATFORM_KEY = "platform"
        private const val EMAIL_KEY = "email"
    }

    fun toJwtClaims(): JwtBuilder =  Jwts.builder()
        .claim(USER_ID_KEY, userId)
        .claim(ROLE_KEY, role)
        .claim(PLATFORM_KEY, loginPlatform)
        .claim(EMAIL_KEY, email)
}
