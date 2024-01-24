package com.newspeed.domain.alarm.application

import com.newspeed.domain.alarm.api.response.AlarmResponse
import com.newspeed.domain.alarm.application.command.AlarmSaveCommand
import com.newspeed.domain.alarm.application.command.AlarmUpdateCommand
import com.newspeed.domain.alarm.domain.Alarm
import com.newspeed.domain.alarm.repository.AlarmRepository
import com.newspeed.domain.user.application.UserService
import com.newspeed.global.exception.alarm.NotFoundAlarmException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.annotation.Validated
import javax.validation.Valid

@Service
@Validated
@Transactional
class AlarmService(
    private val alarmRepository: AlarmRepository,
    private val userService: UserService
) {

    fun saveAlarm(
        @Valid command: AlarmSaveCommand
    ) {
        val user = userService.getUser(command.userId)
        val alarm = command.toEntity(user)

        alarmRepository.save(alarm)
    }

    @Transactional(readOnly = true)
    fun getAlarm(
        userId: Long
    ): AlarmResponse = findAlarmBy(userId)
        .toResponse()

    fun updateAlarm(
        @Valid command: AlarmUpdateCommand
    ) {
        val alarm = findAlarmBy(command.userId)

        alarm.updateTime(command)
    }

    fun deleteAlarm(
        userId: Long
    ) {
        val alarm = findAlarmBy(userId)

        alarm.delete()
    }

    private fun findAlarmBy(
        userId: Long
    ): Alarm {
        val user = userService.getUser(userId)

        return alarmRepository.findByUser(user)
            ?: throw NotFoundAlarmException()
    }
}