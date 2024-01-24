package com.newspeed.domain.alarm.application.command

import com.newspeed.domain.alarm.application.command.validation.AlarmUpdateConstraint
import java.sql.Time
import javax.validation.constraints.Positive

@AlarmUpdateConstraint
data class AlarmUpdateCommand(
    @field:Positive(message = "사용자를 식별할 수 없습니다.")
    val userId: Long,

    val startTime: Time,

    val endTime: Time
)
