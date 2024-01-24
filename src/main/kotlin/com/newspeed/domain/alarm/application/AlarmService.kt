package com.newspeed.domain.alarm.application

import com.newspeed.domain.alarm.application.command.AlarmSaveCommand
import com.newspeed.domain.alarm.repository.AlarmRepository
import com.newspeed.domain.user.application.UserService
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
}