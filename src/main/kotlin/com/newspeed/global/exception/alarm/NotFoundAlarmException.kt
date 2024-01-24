package com.newspeed.global.exception.alarm

import com.newspeed.global.exception.model.ApplicationException
import com.newspeed.global.exception.model.ExceptionType

class NotFoundAlarmException: ApplicationException(
    httpStatus = ExceptionType.NOT_FOUND_ALARM_EXCEPTION.httpStatus,
    message = ExceptionType.NOT_FOUND_ALARM_EXCEPTION.message
)