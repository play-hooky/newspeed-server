package com.newspeed.domain.content.repository

import com.newspeed.domain.content.domain.Content
import org.springframework.data.jpa.repository.JpaRepository

interface ContentRepository: JpaRepository<Content, Long> {
    fun existsByContentIdInPlatformAndUserId(url: String, userId: Long): Boolean

    fun findByUserId(userId: Long): List<Content>
}