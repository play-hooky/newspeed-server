package com.newspeed.global.exception.auth

import com.newspeed.global.exception.model.ApplicationException
import com.newspeed.global.exception.model.ExceptionType

class NotEnoughPermissionException: ApplicationException(
    httpStatus = ExceptionType.NOT_ENOUGH_PERMISSION_EXCEPTION.httpStatus,
    message = ExceptionType.NOT_ENOUGH_PERMISSION_EXCEPTION.message
)