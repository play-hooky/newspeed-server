package com.newspeed.global.exception.search

import com.newspeed.global.exception.model.ApplicationException
import com.newspeed.global.exception.model.ExceptionType

class NotFoundQueryException: ApplicationException(
    httpStatus = ExceptionType.NOT_FOUND_QUERY_EXCEPTION.httpStatus,
    message = ExceptionType.NOT_FOUND_QUERY_EXCEPTION.message
)