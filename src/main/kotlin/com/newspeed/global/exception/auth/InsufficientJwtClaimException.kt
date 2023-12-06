package com.newspeed.global.exception.auth

import com.newspeed.global.exception.model.ApplicationException
import com.newspeed.global.exception.model.ExceptionType

class InsufficientJwtClaimException(
    val field: String
): ApplicationException(
    httpStatus = ExceptionType.INSUFFICIENT_JWT_CLAIM_EXCEPTION.httpStatus,
    message = "$field${ExceptionType.INSUFFICIENT_JWT_CLAIM_EXCEPTION.message}"
)