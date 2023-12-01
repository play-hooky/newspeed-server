package com.newspeed.global.exception.model

class InternalServerException: ApplicationException(
    httpStatus = ExceptionType.INTERNAL_SERVER_EXCEPTION.httpStatus,
    message = ExceptionType.INTERNAL_SERVER_EXCEPTION.message
)