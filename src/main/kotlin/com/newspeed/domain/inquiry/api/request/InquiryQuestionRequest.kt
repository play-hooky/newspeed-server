package com.newspeed.domain.inquiry.api.request

import com.newspeed.domain.inquiry.application.command.InquiryQuestionCommand
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class InquiryQuestionRequest(
    @field:NotBlank(message = "[title] 문의 내용 제목을 입력해주세요.")
    val title: String,

    @field:NotBlank(message = "[body] 문의 내용 본문을 입력해주세요.")
    @field:Size(max = 50000, message = "[body] 문의 내용 본문 길이 제한을 초과하였습니다.")
    val body: String
) {
    fun toCommand(
        userId: Long
    ): InquiryQuestionCommand = InquiryQuestionCommand(
        userId = userId,
        title = title,
        body = body
    )
}
