package com.newspeed.domain.auth.application

import com.newspeed.domain.jwt.application.JwtAuthProvider
import com.newspeed.domain.jwt.domain.JwtConfigProperties
import com.newspeed.template.UnitTestTemplate
import io.jsonwebtoken.Jwts
import org.junit.jupiter.api.Test

class JwtAuthProviderTest: UnitTestTemplate {
    private val jwtConfigProperties = JwtConfigProperties(
        header = "Authorization",
        issuer = "play-hooky",
        secretKey = "798B910610FBB30ADF39647971CF3D4FA4936E5D7697B304F5A4C3A8378A1AFE",
        refreshTokenExpirySeconds = 2592000,
        accessTokenExpirySeconds = 86400
    )

    private val jwtAuthProvider = JwtAuthProvider(
        jwtConfigProperties = jwtConfigProperties
    )

    @Test
    fun access_token_생성을_성공한다() {
        // given
        val authPayload = com.newspeed.domain.auth.domain.AuthPayload(
            userId = 1L,
            role = com.newspeed.domain.auth.domain.Role.USER,
            loginPlatform = com.newspeed.domain.auth.domain.LoginPlatform.KAKAO,
            email = "test@kakao.com"
        )
        val jwtParser = Jwts.parserBuilder()
            .setSigningKey(jwtConfigProperties.secretKey.toByteArray())
            .build()

        // when
        val token = jwtAuthProvider.provideAccessToken(authPayload)

        // then
        assert(jwtParser.isSigned(token))
    }

    @Test
    fun refresh_token_생성을_성공한다() {
        // given
        val authPayload = com.newspeed.domain.auth.domain.AuthPayload(
            userId = 1L,
            role = com.newspeed.domain.auth.domain.Role.USER,
            loginPlatform = com.newspeed.domain.auth.domain.LoginPlatform.KAKAO,
            email = "test@kakao.com"
        )
        val jwtParser = Jwts.parserBuilder()
            .setSigningKey(jwtConfigProperties.secretKey.toByteArray())
            .build()

        // when
        val token = jwtAuthProvider.provideAccessToken(authPayload)

        // then
        assert(jwtParser.isSigned(token))
    }
}