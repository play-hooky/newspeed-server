package com.newspeed.global.exception.category

import com.newspeed.global.exception.model.ApplicationException
import com.newspeed.global.exception.model.ExceptionType

class UnavailableDeleteCategoryException: ApplicationException(
    httpStatus = ExceptionType.UNAVAILABLE_DELETE_CATEGORY_EXCEPTION.httpStatus,
    message = ExceptionType.UNAVAILABLE_DELETE_CATEGORY_EXCEPTION.message
)