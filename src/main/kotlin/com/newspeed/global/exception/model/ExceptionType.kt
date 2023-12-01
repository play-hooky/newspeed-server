package com.newspeed.global.exception.model

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

enum class ExceptionType(
    val httpStatus: HttpStatus,
    val message: String
) {

    // OAuth2
    DUPLICATE_OAUTH2_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "중복된 OAuth 로그인 플랫폼입니다."),
    UNSUPPORTED_OAUTH2_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "지원하지 않는 OAuth 로그인 플랫폼입니다."),
    ILLEGAL_AUTHORIZATION_EXCEPTION(HttpStatus.BAD_REQUEST, message = "지원하지 않는 토큰 타입입니다."),
    NOT_FOUND_TOKEN_EXCEPTION(HttpStatus.BAD_REQUEST, message = "토큰이 존재하지 않아 사용자를 확인할 수 없습니다."),

    // User
    USER_NOT_FOUND_EXCEPTION(HttpStatus.BAD_REQUEST, message = "알 수 없는 사용자입니다."),
    NOT_ENOUGH_PERMISSION_EXCEPTION(HttpStatus.BAD_REQUEST, message = "권한이 부족하여 요청할 수 없는 사용자입니다."),

    // JWT
    EXPIRED_JWT_EXCEPTION(HttpStatus.BAD_REQUEST, "만료된 로그인 토큰입니다."),
    INVALID_JWT_EXCEPTION(HttpStatus.BAD_REQUEST, "올바르지 않은 로그인 토큰입니다."),

    // Internal Server Error
    INTERNAL_SERVER_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내 에러가 발생했습니다."),
    ;

    fun toResponseEntity(): ResponseEntity<ExceptionResponse> = ResponseEntity(
        message.toExceptionResponse(),
        httpStatus
    )
}