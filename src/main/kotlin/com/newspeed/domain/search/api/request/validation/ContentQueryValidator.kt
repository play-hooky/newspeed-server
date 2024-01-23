package com.newspeed.domain.search.api.request.validation

import com.newspeed.domain.search.api.request.ContentSearchRequest
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class ContentQueryValidator: ConstraintValidator<ContentQueryConstraint, ContentSearchRequest> {
    override fun isValid(
        request: ContentSearchRequest,
        context: ConstraintValidatorContext
    ): Boolean =
        ((request.query == null) and (request.order.isDate())) or
        (request.query != null)
}