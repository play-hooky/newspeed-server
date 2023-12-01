package com.newspeed.global.exception.auth

import com.newspeed.global.exception.model.ApplicationException
import com.newspeed.global.exception.model.ExceptionType

class DuplicateOAuth2Exception: ApplicationException(
    httpStatus = ExceptionType.DUPLICATE_OAUTH2_EXCEPTION.httpStatus,
    message = ExceptionType.DUPLICATE_OAUTH2_EXCEPTION.message
)