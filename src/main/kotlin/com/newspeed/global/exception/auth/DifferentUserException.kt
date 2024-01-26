package com.newspeed.global.exception.auth

import com.newspeed.global.exception.model.ApplicationException
import com.newspeed.global.exception.model.ExceptionType

class DifferentUserException: ApplicationException(
    httpStatus = ExceptionType.DIFFERENT_USER_EXCEPTION.httpStatus,
    message = ExceptionType.DIFFERENT_USER_EXCEPTION.message
)