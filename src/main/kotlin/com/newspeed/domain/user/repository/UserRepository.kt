package com.newspeed.domain.user.repository

import com.newspeed.domain.auth.domain.LoginPlatform
import com.newspeed.domain.user.domain.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<User, Long> {
    fun findByPlatformAndEmail(loginPlatform: LoginPlatform, email: String): User?
}