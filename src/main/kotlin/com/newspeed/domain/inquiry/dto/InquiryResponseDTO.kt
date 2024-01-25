package com.newspeed.domain.inquiry.dto

import com.newspeed.domain.inquiry.api.response.InquiryResponse
import com.newspeed.domain.inquiry.domain.InquiryAnswer
import com.newspeed.domain.inquiry.domain.InquiryQuestion
import com.querydsl.core.annotations.QueryProjection

data class InquiryResponseDTO @QueryProjection constructor(
    val question: InquiryQuestion,
    val answer: InquiryAnswer?
) {

    fun toInquiryDTO(): InquiryResponse.InquiryDTO = InquiryResponse.InquiryDTO(
        question = question.toInquiryQuestionDTO(),
        answer = answer?.toInquiryAnswerDTO()
    )
}