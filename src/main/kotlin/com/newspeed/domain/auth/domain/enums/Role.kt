package com.newspeed.domain.auth.domain.enums

import com.newspeed.domain.jwt.annotation.Admin
import com.newspeed.domain.jwt.annotation.Anonymous
import com.newspeed.domain.jwt.annotation.User

enum class Role(
    val annotation: Class<out Annotation>
) {

    ANONYMOUS(Anonymous::class.java),
    USER(User::class.java),
    ADMIN(Admin::class.java);

    companion object {
        fun isUser(
            role: Role
        ): Boolean = role == USER

        fun from(
            role: String
        ): Role = Role.valueOf(role)
    }

    fun isAnonymous(): Boolean = this == ANONYMOUS
}