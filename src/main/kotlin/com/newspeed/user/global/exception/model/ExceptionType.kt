package com.newspeed.user.global.exception.model

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

enum class ExceptionType(
    val httpStatus: HttpStatus,
    val message: String
) {

    // OAuth2
    DUPLICATE_OAUTH2_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "중복된 OAuth 로그인 플랫폼입니다."),
    UNSUPPORTED_OAUTH2_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "지원하지 않는 OAuth 로그인 플랫폼입니다."),

    // Internal Server Error
    INTERNAL_SERVER_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내 에러가 발생했습니다.");

    fun toResponseEntity(): ResponseEntity<ExceptionResponse> = ResponseEntity(
        message.toExceptionResponse(),
        httpStatus
    )
}