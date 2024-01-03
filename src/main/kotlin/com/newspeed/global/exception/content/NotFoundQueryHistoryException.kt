package com.newspeed.global.exception.content

import com.newspeed.global.exception.model.ApplicationException
import com.newspeed.global.exception.model.ExceptionType

class NotFoundQueryHistoryException: ApplicationException(
    httpStatus = ExceptionType.NOT_FOUND_QUERY_HISTORY_EXCEPTION.httpStatus,
    message = ExceptionType.NOT_FOUND_QUERY_HISTORY_EXCEPTION.message
)