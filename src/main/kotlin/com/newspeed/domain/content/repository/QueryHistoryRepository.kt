package com.newspeed.domain.content.repository

import com.newspeed.domain.content.domain.QueryHistory
import com.newspeed.domain.user.domain.User
import org.springframework.data.jpa.repository.JpaRepository

interface QueryHistoryRepository: JpaRepository<QueryHistory, Long>, QueryHistoryCustomRepository {
    fun findByUser(user: User): List<QueryHistory>
}