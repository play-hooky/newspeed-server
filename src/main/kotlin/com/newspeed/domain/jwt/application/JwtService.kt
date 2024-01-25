package com.newspeed.domain.jwt.application

import com.newspeed.domain.auth.domain.AuthPayload
import com.newspeed.domain.jwt.domain.RefreshToken
import com.newspeed.domain.jwt.dto.IssuedJwtDTO
import com.newspeed.domain.jwt.repository.RefreshTokenRepository
import com.newspeed.global.exception.auth.NotFoundTokenException
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

    fun deleteRefreshToken(
        userId: Long
    ) {
        val refreshToken = getRefreshToken(userId)

        refreshTokenRepository.deleteAll(refreshToken)
    }

    private fun getRefreshToken(
        userId: Long
    ): List<RefreshToken> = refreshTokenRepository.findByUserId(userId)
        .takeIf { it.isNotEmpty() }
        ?: throw NotFoundTokenException()
}