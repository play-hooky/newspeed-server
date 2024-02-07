package com.newspeed.support

import com.newspeed.domain.auth.domain.enums.Role

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class MockUser(
    val id: Long = 1,
    val role: Role = Role.USER
)
