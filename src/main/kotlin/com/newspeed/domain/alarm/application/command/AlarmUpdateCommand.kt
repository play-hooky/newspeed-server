package com.newspeed.domain.alarm.application.command

import java.sql.Time
import javax.validation.constraints.Positive

data class AlarmUpdateCommand(
    @field:Positive(message = "사용자를 식별할 수 없습니다.")
    val userId: Long,

    val startTime: Time,

    val endTime: Time
)
