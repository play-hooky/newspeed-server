package com.newspeed.global.exception.content

import com.newspeed.global.exception.model.ApplicationException
import com.newspeed.global.exception.model.ExceptionType

class UnavailableDeleteQueryHistoryException: ApplicationException(
    httpStatus = ExceptionType.UNAVAILABLE_DELETE_QUERY_HISTORY_EXCEPTION.httpStatus,
    message = ExceptionType.UNAVAILABLE_DELETE_QUERY_HISTORY_EXCEPTION.message
)