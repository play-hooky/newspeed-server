package com.newspeed.domain.alarm.application.command.validation

import com.newspeed.domain.alarm.application.command.AlarmUpdateCommand
import com.newspeed.domain.alarm.repository.AlarmRepository
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class AlarmUpdateValidator(
    private val alarmRepository: AlarmRepository
): ConstraintValidator<AlarmUpdateConstraint, AlarmUpdateCommand> {
    override fun isValid(
        command: AlarmUpdateCommand,
        context: ConstraintValidatorContext
    ): Boolean = alarmRepository.existsByUserId(command.userId)
}
