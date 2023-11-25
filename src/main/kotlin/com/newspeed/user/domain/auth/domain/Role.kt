package com.newspeed.user.domain.auth.domain

import com.newspeed.user.domain.auth.annotation.Admin
import com.newspeed.user.domain.auth.annotation.Anonymous
import com.newspeed.user.domain.auth.annotation.User

enum class Role(
    private val annotation: Class<out Annotation>
) {

    ANONYMOUS(Anonymous::class.java),
    USER(User::class.java),
    ADMIN(Admin::class.java);
}