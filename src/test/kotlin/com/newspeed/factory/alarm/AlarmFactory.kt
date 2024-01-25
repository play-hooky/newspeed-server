package com.newspeed.factory.alarm

import com.newspeed.domain.alarm.api.response.AlarmResponse
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

        fun createAlarmResponse(
            startTime: Time,
            endTime: Time
        ) = AlarmResponse(
            startTime = "${getHour(startTime)}:${getMinute(startTime)}",
            endTime = "${getHour(endTime)}:${getMinute(endTime)}"
        )

        private fun getHour(
            time: Time
        ): String = String.format("%02d", time.toLocalTime().hour)

        private fun getMinute(
            time: Time
        ): String = String.format("%02d", time.toLocalTime().minute)
    }
}