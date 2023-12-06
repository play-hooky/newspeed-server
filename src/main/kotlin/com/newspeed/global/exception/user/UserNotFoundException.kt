package com.newspeed.global.exception.user

import com.newspeed.global.exception.model.ApplicationException
import com.newspeed.global.exception.model.ExceptionType

class UserNotFoundException: ApplicationException(
    httpStatus = ExceptionType.USER_NOT_FOUND_EXCEPTION.httpStatus,
    message = ExceptionType.USER_NOT_FOUND_EXCEPTION.message
)