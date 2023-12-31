package com.newspeed.domain.jwt.application

import com.newspeed.domain.auth.domain.AuthPayload
import com.newspeed.domain.jwt.domain.RefreshToken
import com.newspeed.domain.jwt.dto.IssuedJwtDTO
import com.newspeed.domain.jwt.repository.RefreshTokenRepository
import org.springframework.stereotype.Service

@Service
class JwtService(
    private val jwtAuthProvider: JwtAuthProvider,
    private val refreshTokenRepository: RefreshTokenRepository
) {

    fun issueAllJwt(
        authPayload: AuthPayload
    ): IssuedJwtDTO = jwtAuthProvider.issueAllJwt(authPayload)

    fun saveRefreshToken(
        userId: Long,
        refreshToken: String
    ) {
        val refreshToken = RefreshToken(
            userId = userId,
            token = refreshToken
        )

        refreshTokenRepository.save(refreshToken)
    }

    fun reissueAccessToken(
        authPayload: AuthPayload
    ): String = jwtAuthProvider.provideAccessToken(authPayload)
}