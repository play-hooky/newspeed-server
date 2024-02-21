package com.newspeed.domain.auth.application

import com.newspeed.domain.auth.domain.AuthenticateContext
import com.newspeed.domain.auth.domain.enums.Role
import com.newspeed.domain.jwt.application.JwtExtractor
import com.newspeed.domain.jwt.application.TokenExtractor
import com.newspeed.global.exception.auth.NotEnoughPermissionException
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class AuthInterceptor(
    private val authenticateContext: AuthenticateContext,
    private val tokenExtractor: TokenExtractor,
    @Qualifier("jwtAuthExtractor") private val jwtExtractor: JwtExtractor
): HandlerInterceptor {

    companion object {
        private val ALLOW_ANONYMOUS_URLS = listOf(
            "/contents/search"
        )
    }

    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any
    ): Boolean {
        if (ALLOW_ANONYMOUS_URLS.contains(request.requestURI)) {
            runCatching { setAuthenticate(request) }
            return true
        }

        setAuthenticate(request)
        return true
    }

    private fun setAuthenticate(
        request: HttpServletRequest
    ) {
        val token = tokenExtractor.extract(request)
        val payload = jwtExtractor.extract(token)
            .takeIf { Role.isUser(it.role) }
            ?: throw NotEnoughPermissionException()

        authenticateContext.setAuthenticate(payload)
    }
}