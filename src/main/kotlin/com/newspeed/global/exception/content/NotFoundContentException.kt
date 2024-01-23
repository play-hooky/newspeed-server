package com.newspeed.global.exception.content

import com.newspeed.global.exception.model.ApplicationException
import com.newspeed.global.exception.model.ExceptionType

class NotFoundContentException: ApplicationException(
    httpStatus = ExceptionType.NOT_FOUND_CONTENT_EXCEPTION.httpStatus,
    message = ExceptionType.NOT_FOUND_CONTENT_EXCEPTION.message
)