package com.newspeed.domain.auth.domain

import com.newspeed.domain.jwt.annotation.Admin
import com.newspeed.domain.jwt.annotation.Anonymous
import com.newspeed.domain.jwt.annotation.User

enum class Role(
    private val annotation: Class<out Annotation>
) {

    ANONYMOUS(Anonymous::class.java),
    USER(User::class.java),
    ADMIN(Admin::class.java);
}