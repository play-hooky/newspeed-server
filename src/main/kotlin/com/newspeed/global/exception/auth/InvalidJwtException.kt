package com.newspeed.global.exception.auth

import com.newspeed.global.exception.model.ApplicationException
import com.newspeed.global.exception.model.ExceptionType

class InvalidJwtException: ApplicationException(
    httpStatus = ExceptionType.INVALID_JWT_EXCEPTION.httpStatus,
    message = ExceptionType.INVALID_JWT_EXCEPTION.message
)