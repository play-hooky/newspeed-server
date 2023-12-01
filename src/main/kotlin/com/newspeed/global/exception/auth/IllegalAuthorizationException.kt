package com.newspeed.global.exception.auth

import com.newspeed.global.exception.model.ApplicationException
import com.newspeed.global.exception.model.ExceptionType

class IllegalAuthorizationException: ApplicationException(
    httpStatus = ExceptionType.ILLEGAL_AUTHORIZATION_EXCEPTION.httpStatus,
    message = ExceptionType.ILLEGAL_AUTHORIZATION_EXCEPTION.message
)