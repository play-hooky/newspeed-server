package com.newspeed.global.exception.category

import com.newspeed.global.exception.model.ApplicationException
import com.newspeed.global.exception.model.ExceptionType

class NotFoundCategoryException: ApplicationException(
    httpStatus = ExceptionType.NOT_FOUND_CATEGORY_EXCEPTION.httpStatus,
    message = ExceptionType.NOT_FOUND_CATEGORY_EXCEPTION.message
)