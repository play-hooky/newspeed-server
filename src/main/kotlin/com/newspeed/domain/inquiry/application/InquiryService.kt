package com.newspeed.domain.inquiry.application

import com.newspeed.domain.inquiry.api.response.InquiryResponse
import com.newspeed.domain.inquiry.api.response.toInquiryResponse
import com.newspeed.domain.inquiry.application.command.InquiryQuestionCommand
import com.newspeed.domain.inquiry.repository.InquiryQuestionRepository
import com.newspeed.domain.user.application.UserService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.annotation.Validated
import javax.validation.Valid

@Service
@Transactional
@Validated
class InquiryService(
    private val inquiryQuestionRepository: InquiryQuestionRepository,
    private val userService: UserService
) {

    fun saveInquiryQuestion(
        @Valid command: InquiryQuestionCommand
    ) {
        val user = userService.getUser(command.userId)
        val inquiryQuestion = command.toEntity(user)

        inquiryQuestionRepository.save(inquiryQuestion)
    }

    @Transactional(readOnly = true)
    fun getInquiry(
        userId: Long
    ): InquiryResponse = inquiryQuestionRepository.findInquiryByUserId(userId)
        .map { it.toInquiryDTO() }
        .toInquiryResponse()
}