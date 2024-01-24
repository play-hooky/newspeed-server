package com.newspeed.domain.alarm.api.request

import com.newspeed.domain.alarm.application.command.AlarmSaveCommand
import com.newspeed.global.enums.toTime
import javax.validation.constraints.Pattern

data class AlarmSaveRequest(
    @field:Pattern(regexp = "^([01]\\d|2[0-3]):?([0-5]\\d)\$", message = "[startTime] 알람 시작 시간을 시간 형식에 맞추어 입력해주세요")
    val startTime: String,

    @field:Pattern(regexp = "^([01]\\d|2[0-3]):?([0-5]\\d)\$", message = "[endTime] 알람 종료 시간을 시간 형식에 맞추어 입력해주세요")
    val endTime: String
) {
    fun toCommand(
        userId: Long
    ): AlarmSaveCommand = AlarmSaveCommand(
        userId = userId,
        startTime = startTime.toTime(),
        endTime = endTime.toTime()
    )
}
