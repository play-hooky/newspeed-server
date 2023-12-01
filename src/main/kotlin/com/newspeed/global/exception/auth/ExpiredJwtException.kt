package com.newspeed.global.exception.auth

import com.newspeed.global.exception.model.ApplicationException
import com.newspeed.global.exception.model.ExceptionType

class ExpiredJwtException: ApplicationException(
    httpStatus = ExceptionType.EXPIRED_JWT_EXCEPTION.httpStatus,
    message = ExceptionType.EXPIRED_JWT_EXCEPTION.message
)