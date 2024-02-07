package com.newspeed.global.exception.auth

import com.newspeed.global.exception.model.ApplicationException
import com.newspeed.global.exception.model.ExceptionType

class OAuth2LoginException: ApplicationException(
    httpStatus = ExceptionType.OAUTH2_LOGIN_EXCEPTION.httpStatus,
    message = ExceptionType.OAUTH2_LOGIN_EXCEPTION.message
)