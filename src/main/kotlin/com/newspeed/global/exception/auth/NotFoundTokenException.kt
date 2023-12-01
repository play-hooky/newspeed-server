package com.newspeed.global.exception.auth

import com.newspeed.global.exception.model.ApplicationException
import com.newspeed.global.exception.model.ExceptionType

class NotFoundTokenException: ApplicationException(
    httpStatus = ExceptionType.NOT_FOUND_TOKEN_EXCEPTION.httpStatus,
    message = ExceptionType.NOT_FOUND_TOKEN_EXCEPTION.message
)