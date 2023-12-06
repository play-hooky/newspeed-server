package com.newspeed.domain.user.application

import com.newspeed.domain.auth.domain.AuthenticateContext
import com.newspeed.domain.auth.domain.enums.Role
import com.newspeed.global.exception.auth.NotEnoughPermissionException
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

class RoleArgumentResolver(
    private val role: Role,
    private val authenticateContext: AuthenticateContext
): HandlerMethodArgumentResolver {
    override fun supportsParameter(
        parameter: MethodParameter
    ): Boolean = (parameter.parameterType.equals(Long::class.java)) and
            (parameter.hasParameterAnnotation(role.annotation))

    override fun resolveArgument(
        parameter: MethodParameter?,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest?,
        binderFactory: WebDataBinderFactory?
    ): Long = authenticateContext.userId().takeIf { authenticateContext.role() == this.role }
        ?: throw NotEnoughPermissionException()
}