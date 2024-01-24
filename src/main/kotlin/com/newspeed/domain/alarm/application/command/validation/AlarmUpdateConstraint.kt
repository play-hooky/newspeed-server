package com.newspeed.domain.alarm.application.command.validation

import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@Constraint(validatedBy = [AlarmUpdateValidator::class])
@Retention(AnnotationRetention.RUNTIME)
annotation class AlarmUpdateConstraint(
    val message: String = "해당 사용자는 등록된 알람이 존재하지 않습니다.",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
