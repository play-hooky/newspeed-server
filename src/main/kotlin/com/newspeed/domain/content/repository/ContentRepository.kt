package com.newspeed.domain.content.repository

import com.newspeed.domain.content.domain.Content
import com.newspeed.domain.user.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface ContentRepository: JpaRepository<Content, Long> {
    fun existsByUserIdAndContentIdInPlatform(userId: Long, contentIdInPlatform: String): Boolean

    fun findByUserAndUrl(user: User, url: String): List<Content>

    fun findByUserId(userId: Long): List<Content>

    @Modifying
    @Query("update Content content set content.deletedAt = now() where content.id in :ids")
    fun deleteByIds(ids: List<Long>)
}