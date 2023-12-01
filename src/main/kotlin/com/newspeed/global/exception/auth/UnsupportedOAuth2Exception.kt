package com.newspeed.global.exception.auth

import com.newspeed.global.exception.model.ApplicationException
import com.newspeed.global.exception.model.ExceptionType

class UnsupportedOAuth2Exception: ApplicationException(
    httpStatus = ExceptionType.UNSUPPORTED_OAUTH2_EXCEPTION.httpStatus,
    message = ExceptionType.UNSUPPORTED_OAUTH2_EXCEPTION.message
)