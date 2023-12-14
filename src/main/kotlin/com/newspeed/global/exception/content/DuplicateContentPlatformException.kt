package com.newspeed.global.exception.content

import com.newspeed.global.exception.model.ApplicationException
import com.newspeed.global.exception.model.ExceptionType

class DuplicateContentPlatformException: ApplicationException(
    httpStatus = ExceptionType.DUPLICATE_CONTENT_PLATFORM_EXCEPTION.httpStatus,
    message = ExceptionType.DUPLICATE_CONTENT_PLATFORM_EXCEPTION.message
)