package com.newspeed.domain.content.application.command.validation

import com.newspeed.domain.content.application.command.ContentSaveCommand
import com.newspeed.domain.content.repository.ContentRepository
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class ContentSaveCommandValidator(
    private val contentRepository: ContentRepository
): ConstraintValidator<ContentSaveCommandConstraint, ContentSaveCommand> {
    override fun isValid(
        command: ContentSaveCommand,
        context: ConstraintValidatorContext
    ): Boolean = !contentRepository.existsByUrlAndUserId(command.url, command.userId)
}