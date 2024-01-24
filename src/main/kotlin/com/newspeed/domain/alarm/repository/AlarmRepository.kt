package com.newspeed.domain.alarm.repository

import com.newspeed.domain.alarm.domain.Alarm
import com.newspeed.domain.user.domain.User
import org.springframework.data.jpa.repository.JpaRepository

interface AlarmRepository: JpaRepository<Alarm, Long> {
    fun existsByUserId(userId: Long): Boolean

    fun findByUser(user: User): Alarm?
}