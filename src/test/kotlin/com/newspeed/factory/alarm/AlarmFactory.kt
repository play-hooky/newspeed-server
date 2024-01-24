package com.newspeed.factory.alarm

import com.newspeed.domain.alarm.domain.Alarm
import com.newspeed.domain.user.domain.User
import java.sql.Time
import java.time.LocalTime

class AlarmFactory {

    companion object {
        fun createAlarm(
            user: User
        ) = Alarm(
            user = user,
            startTime = Time.valueOf(LocalTime.now()),
            endTime = Time.valueOf(LocalTime.now().plusHours(2))
        )
    }
}