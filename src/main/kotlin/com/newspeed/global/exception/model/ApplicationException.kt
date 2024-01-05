package com.newspeed.global.exception.model

import org.springframework.beans.BeanInstantiationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.BindException
import javax.validation.ConstraintViolationException

open class ApplicationException(
    private val httpStatus: HttpStatus,
    override val message: String
): RuntimeException(message) {

    fun toResponseEntity(): ResponseEntity<ExceptionResponse> = ResponseEntity(
        message.toExceptionResponse(),
        httpStatus
    )
}

fun String.toExceptionResponse(): ExceptionResponse = ExceptionResponse(
    errorMessage = this
)

fun BindException.toResponseEntity(): ResponseEntity<ExceptionResponse> = ResponseEntity(
    (this.fieldError?.defaultMessage ?: ExceptionType.BIND_EXCEPTION.message).toExceptionResponse(),
    ExceptionType.BIND_EXCEPTION.httpStatus
)

fun BeanInstantiationException.toResponseEntity(): ResponseEntity<ExceptionResponse> {
    val errorField = this.cause?.localizedMessage?.split("parameter ")?.lastOrNull()
    val message = if (errorField == null) ExceptionType.BIND_EXCEPTION.message else "${errorField}를 입력해주세요."

    return ResponseEntity(
        message.toExceptionResponse(),
        ExceptionType.BIND_EXCEPTION.httpStatus
    )
}

fun NotImplementedError.toResponseEntity(): ResponseEntity<ExceptionResponse> = ResponseEntity(
    "현재 구현중입니다. 잠시만 기다려주세요.".toExceptionResponse(),
    ExceptionType.INTERNAL_SERVER_EXCEPTION.httpStatus
)

fun HttpMessageNotReadableException.toResponseEntity(): ResponseEntity<ExceptionResponse> = ResponseEntity(
    this.message?.toExceptionResponse() ?: ExceptionType.INTERNAL_SERVER_EXCEPTION.message.toExceptionResponse(),
    HttpStatus.BAD_REQUEST
)

fun ConstraintViolationException.toResponseEntity(): ResponseEntity<ExceptionResponse> = ResponseEntity(
    this.constraintViolations
        .map { it.messageTemplate }
        .first()
        .toExceptionResponse(),
    HttpStatus.BAD_REQUEST
)