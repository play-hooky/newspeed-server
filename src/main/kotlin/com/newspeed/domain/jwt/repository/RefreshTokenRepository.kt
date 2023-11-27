package com.newspeed.domain.jwt.repository

import com.newspeed.domain.jwt.domain.RefreshToken
import org.springframework.data.repository.CrudRepository

interface RefreshTokenRepository: CrudRepository<RefreshToken, Long> {
}