package com.newspeed.user.global.exception.auth

import com.newspeed.user.global.exception.model.ApplicationException
import com.newspeed.user.global.exception.model.ExceptionType

class UnsupportedOAuth2Exception: ApplicationException(
    httpStatus = ExceptionType.UNSUPPORTED_OAUTH2_EXCEPTION.httpStatus,
    message = ExceptionType.UNSUPPORTED_OAUTH2_EXCEPTION.message
)