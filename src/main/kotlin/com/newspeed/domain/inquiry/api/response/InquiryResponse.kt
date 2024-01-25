package com.newspeed.domain.inquiry.api.response

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.newspeed.domain.inquiry.application.serde.InquiryResponseSerializer
import java.time.LocalDateTime

@JsonSerialize(using = InquiryResponseSerializer::class)
data class InquiryResponse(
    val inquiries: List<InquiryDTO>
) {
    data class InquiryDTO(
        val question: InquiryQuestionDTO,
        val answer: InquiryAnswerDTO?
    ) {
        data class InquiryQuestionDTO(
            val title: String,
            val body: String,
            val createdAt: LocalDateTime
        )

        data class InquiryAnswerDTO(
            val body: String,
            val createdAt: LocalDateTime
        )
    }
}

fun List<InquiryResponse.InquiryDTO>.toInquiryResponse() = InquiryResponse(
    inquiries = this
)