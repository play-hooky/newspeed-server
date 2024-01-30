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
    UNAVAILABLE_EMAIL_EXCEPTION(HttpStatus.BAD_REQUEST, message = "이메일을 확인할 수 없습니다. 로그인 시에 이메일 제공을 허용해주세요."),
    DIFFERENT_USER_EXCEPTION(HttpStatus.BAD_REQUEST, "로그인한 회원과 회원 탈퇴 요청 회원가 일치하지 않습니다."),

    // JWT
    EXPIRED_JWT_EXCEPTION(HttpStatus.BAD_REQUEST, "만료된 로그인 토큰입니다."),
    INVALID_JWT_EXCEPTION(HttpStatus.BAD_REQUEST, "올바르지 않은 로그인 토큰입니다."),
    INSUFFICIENT_JWT_CLAIM_EXCEPTION(HttpStatus.BAD_REQUEST, message = " 정보가 누락된 비정상 토큰입니다."),

    // Search
    NOT_FOUND_QUERY_EXCEPTION(HttpStatus.BAD_REQUEST, "검색어를 입력해주세요."),

    // Content
    DUPLICATE_CONTENT_PLATFORM_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "중복된 컨텐츠 검색 플랫폼입니다."),
    UNSUPPORTED_CONTENT_PLATFORM_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "지원하지 않는 OAuth 로그인 플랫폼입니다."),
    NOT_FOUND_QUERY_HISTORY_EXCEPTION(HttpStatus.BAD_REQUEST, "해당 검색 기록을 조회할 수 없습니다."),
    UNAVAILABLE_DELETE_QUERY_HISTORY_EXCEPTION(HttpStatus.BAD_REQUEST, "해당 사용자는 해당 검색 기록을 삭제할 수 없습니다."),
    UNAVAILABLE_SAVE_QUERY_HISTORY_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "해당 검색어를 저장할 수 없습니다."),
    NOT_FOUND_CONTENT_EXCEPTION(HttpStatus.BAD_REQUEST, "해당 컨텐츠를 조회할 수 없어 삭제가 불가능합니다."),

    // Alarm
    NOT_FOUND_ALARM_EXCEPTION(HttpStatus.BAD_REQUEST, "알람을 조회할 수 없습니다."),

    // Category
    NOT_FOUND_CATEGORY_EXCEPTION(HttpStatus.BAD_REQUEST, "해당 카테고리를 조회할 수 없습니다."),
    UNAVAILABLE_DELETE_CATEGORY_EXCEPTION(HttpStatus.BAD_REQUEST, "해당 사용자는 해당 카테고리를 삭제할 수 없습니다."),

    // Internal Server Error
    INTERNAL_SERVER_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내 에러가 발생했습니다."),

    // Client
    BIND_EXCEPTION(HttpStatus.BAD_REQUEST, "올바르지 않은 입력값입니다.")
    ;

    fun toResponseEntity(): ResponseEntity<ExceptionResponse> = ResponseEntity(
        message.toExceptionResponse(),
        httpStatus
    )
}