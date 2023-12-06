package com.newspeed.domain.auth.domain

import com.newspeed.domain.auth.domain.enums.Role
import org.springframework.stereotype.Component
import org.springframework.web.context.annotation.RequestScope


@Component
@RequestScope
class AuthenticateContext {
    private var userId: Long = 0
    private var role = Role.ANONYMOUS

    fun setAuthenticate(
        authPayload: AuthPayload
    ) {
        this.userId = authPayload.userId
        this.role = authPayload.role
    }

    fun userId(): Long = userId

    fun role(): Role = role
}

