package com.newspeed.domain.jwt.application

import com.newspeed.global.exception.auth.IllegalAuthorizationException
import com.newspeed.global.exception.auth.NotFoundTokenException
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest

@Component
class HeaderTokenExtractor: TokenExtractor {
    companion object {
        private const val BEARER_TOKEN_PREFIX = "Bearer "
    }
    override fun extract(
        request: HttpServletRequest
    ): String {
        val authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION)
            ?: throw NotFoundTokenException()

        val token = extractToken(authorizationHeader)

        return token.takeIf { token.isNotBlank() }
            ?: throw NotFoundTokenException()
    }

    private fun extractToken(
        header: String
    ): String = header
        .takeIf { isValidTokenType(it) }
        ?.substring(BEARER_TOKEN_PREFIX.length)?.trim()
        ?: throw IllegalAuthorizationException()


    private fun isValidTokenType(
        header: String
    ): Boolean = header.lowercase().startsWith(BEARER_TOKEN_PREFIX.lowercase())
}