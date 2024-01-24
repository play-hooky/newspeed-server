package com.newspeed.domain.alarm.application.command.validation

import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@Constraint(validatedBy = [AlarmSaveValidator::class])
@Retention(AnnotationRetention.RUNTIME)
annotation class AlarmSaveConstraint(
    val message: String = "해당 사용자는 이미 알람이 등록되어 있습니다.",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
