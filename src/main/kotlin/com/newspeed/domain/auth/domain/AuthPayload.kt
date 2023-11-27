package com.newspeed.domain.auth.domain

import io.jsonwebtoken.JwtBuilder
import io.jsonwebtoken.Jwts

data class AuthPayload(
    val userId: Long,
    val role: com.newspeed.domain.auth.domain.Role,
    val loginPlatform: com.newspeed.domain.auth.domain.LoginPlatform,
    val email: String
) {
    companion object {
        private const val USER_ID_KEY = "userId"
        private const val ROLE_KEY = "role"
        private const val PLATFORM_KEY = "platform"
        private const val EMAIL_KEY = "email"
    }

    fun toJwtClaims(): JwtBuilder =  Jwts.builder()
        .claim(com.newspeed.domain.auth.domain.AuthPayload.Companion.USER_ID_KEY, userId)
        .claim(com.newspeed.domain.auth.domain.AuthPayload.Companion.ROLE_KEY, role)
        .claim(com.newspeed.domain.auth.domain.AuthPayload.Companion.PLATFORM_KEY, loginPlatform)
        .claim(com.newspeed.domain.auth.domain.AuthPayload.Companion.EMAIL_KEY, email)
}
