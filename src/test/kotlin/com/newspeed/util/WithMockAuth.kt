package com.newspeed.util

import com.newspeed.domain.auth.domain.enums.Role

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class WithMockAuth(
    val userId: Long = 1,
    val role: Role = Role.USER
)