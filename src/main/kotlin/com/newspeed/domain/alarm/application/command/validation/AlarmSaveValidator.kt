package com.newspeed.domain.alarm.application.command.validation

import com.newspeed.domain.alarm.application.command.AlarmSaveCommand
import com.newspeed.domain.alarm.repository.AlarmRepository
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class AlarmSaveValidator(
    private val alarmRepository: AlarmRepository
): ConstraintValidator<AlarmSaveConstraint, AlarmSaveCommand> {
    override fun isValid(
        command: AlarmSaveCommand,
        context: ConstraintValidatorContext
    ): Boolean = !alarmRepository.existsByUserId(command.userId)
}