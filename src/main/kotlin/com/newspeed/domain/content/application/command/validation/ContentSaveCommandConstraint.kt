package com.newspeed.domain.content.application.command.validation

import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@Constraint(validatedBy = [ContentSaveCommandValidator::class])
@Retention(AnnotationRetention.RUNTIME)
annotation class ContentSaveCommandConstraint(
    val message: String = "이미 등록된 컨텐츠입니다",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
