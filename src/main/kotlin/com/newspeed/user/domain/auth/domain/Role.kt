package com.newspeed.user.domain.auth.domain

import com.newspeed.user.domain.jwt.annotation.Admin
import com.newspeed.user.domain.jwt.annotation.Anonymous
import com.newspeed.user.domain.jwt.annotation.User

enum class Role(
    private val annotation: Class<out Annotation>
) {

    ANONYMOUS(Anonymous::class.java),
    USER(User::class.java),
    ADMIN(Admin::class.java);
}