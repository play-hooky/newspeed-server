package com.newspeed.global.exception.handler

import com.newspeed.global.exception.model.ApplicationException
import com.newspeed.global.exception.model.ExceptionResponse
import com.newspeed.global.exception.model.ExceptionType
import com.newspeed.global.exception.model.toResponseEntity
import org.slf4j.LoggerFactory
import org.springframework.beans.BeanInstantiationException
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.BindException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import javax.validation.ConstraintViolationException

@RestControllerAdvice
class GlobalExceptionHandler {
    companion object {
        private const val LOG_FORMAT = "Class : {}, ErrorMessage : {}"
        private val log = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)
    }

    @ExceptionHandler(NotImplementedError::class)
    fun notImplementedError(
        e: NotImplementedError
    ): ResponseEntity<ExceptionResponse> {
        val errorMessage = e.stackTraceToString()
        log.warn(LOG_FORMAT, e.javaClass.getSimpleName(), errorMessage)

        return e.toResponseEntity()
    }

    @ExceptionHandler(BeanInstantiationException::class)
    fun beanInstantiationException(
        e: BeanInstantiationException
    ): ResponseEntity<ExceptionResponse> {
        val errorMessage = e.message
        log.warn(LOG_FORMAT, e.javaClass.getSimpleName(), errorMessage)

        return e.toResponseEntity()
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun httpMessageNotReadableException(
        e: HttpMessageNotReadableException
    ): ResponseEntity<ExceptionResponse> {
        val errorMessage = e.message
        log.warn(LOG_FORMAT, e.javaClass.getSimpleName(), errorMessage)

        return e.toResponseEntity()
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun constraintViolationException(
        e: ConstraintViolationException
    ): ResponseEntity<ExceptionResponse> {
        val errorMessage = e.message

        log.warn(LOG_FORMAT, e.javaClass.getSimpleName(), errorMessage)

        return e.toResponseEntity()
    }

    @ExceptionHandler(BindException::class)
    fun bindException(
        e: BindException
    ): ResponseEntity<ExceptionResponse> {
        val errorMessage = e.message
        log.warn(LOG_FORMAT, e.javaClass.getSimpleName(), errorMessage)

        return e.toResponseEntity()
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
        log.error(LOG_FORMAT, e.javaClass.getSimpleName(), e.stackTraceToString())
        return ExceptionType.INTERNAL_SERVER_EXCEPTION.toResponseEntity()
    }
}