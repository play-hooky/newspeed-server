package com.newspeed.domain.alarm.application.command

import com.newspeed.domain.alarm.application.command.validation.AlarmSaveConstraint
import com.newspeed.domain.alarm.domain.Alarm
import com.newspeed.domain.user.domain.User
import java.sql.Time
import javax.validation.constraints.Positive

@AlarmSaveConstraint
data class AlarmSaveCommand(
    @field:Positive(message = "사용자를 식별할 수 없습니다.")
    val userId: Long,

    val startTime: Time,

    val endTime: Time
) {
    fun toEntity(
        user: User
    ): Alarm = Alarm(
        user = user,
        startTime = startTime,
        endTime = endTime
    )
}
