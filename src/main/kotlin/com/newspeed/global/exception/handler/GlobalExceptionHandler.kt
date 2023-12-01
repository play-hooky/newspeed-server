package com.newspeed.global.exception.handler

import com.newspeed.global.exception.model.ApplicationException
import com.newspeed.global.exception.model.ExceptionResponse
import com.newspeed.global.exception.model.ExceptionType
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    companion object {
        private const val LOG_FORMAT = "Class : {}, ErrorMessage : {}"
        private val log = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)
    }

    @ExceptionHandler(ApplicationException::class)
    fun applicationException(
        e: ApplicationException
    ): ResponseEntity<ExceptionResponse> {
        val errorMessage: String = e.message
        log.warn(LOG_FORMAT, e.javaClass.getSimpleName(), errorMessage)

        return e.toResponseEntity()
    }

    @ExceptionHandler(RuntimeException::class)
    fun runtimeException(
        e: RuntimeException
    ): ResponseEntity<ExceptionResponse> {
        log.error(LOG_FORMAT, e.javaClass.getSimpleName(), e.message)
        return ExceptionType.INTERNAL_SERVER_EXCEPTION.toResponseEntity()
    }
}