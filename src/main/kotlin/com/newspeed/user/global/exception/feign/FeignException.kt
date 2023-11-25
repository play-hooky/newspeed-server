package com.newspeed.user.global.exception.feign

import com.newspeed.user.global.exception.model.ApplicationException
import com.newspeed.user.global.exception.model.ExceptionType

class FeignException(
    override val message: String
): ApplicationException(
    httpStatus = ExceptionType.INTERNAL_SERVER_EXCEPTION.httpStatus,
    message = message
) {
}