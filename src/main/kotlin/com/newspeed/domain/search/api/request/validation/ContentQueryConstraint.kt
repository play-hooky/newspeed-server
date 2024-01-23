package com.newspeed.domain.search.api.request.validation

import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@Constraint(validatedBy = [ContentQueryValidator::class])
@Retention(AnnotationRetention.RUNTIME)
annotation class ContentQueryConstraint(
    val message: String = "[order] 검색어가 없을땐 최신순(= date)으로 검색해주세요",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)