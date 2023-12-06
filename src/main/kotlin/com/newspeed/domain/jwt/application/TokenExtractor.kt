package com.newspeed.domain.jwt.application

import javax.servlet.http.HttpServletRequest

interface TokenExtractor {
    fun extract(request: HttpServletRequest): String
}