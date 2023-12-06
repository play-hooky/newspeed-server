package com.newspeed.global.exception.user

import com.newspeed.global.exception.model.ApplicationException
import com.newspeed.global.exception.model.ExceptionType

class UnavailableEmailException: ApplicationException(
    httpStatus = ExceptionType.UNAVAILABLE_EMAIL_EXCEPTION.httpStatus,
    message = ExceptionType.UNAVAILABLE_EMAIL_EXCEPTION.message
)