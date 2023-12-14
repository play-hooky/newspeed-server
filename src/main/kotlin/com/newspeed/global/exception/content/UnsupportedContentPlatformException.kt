package com.newspeed.global.exception.content

import com.newspeed.global.exception.model.ApplicationException
import com.newspeed.global.exception.model.ExceptionType

class UnsupportedContentPlatformException: ApplicationException(
    httpStatus = ExceptionType.UNSUPPORTED_CONTENT_PLATFORM_EXCEPTION.httpStatus,
    message = ExceptionType.UNSUPPORTED_CONTENT_PLATFORM_EXCEPTION.message
)