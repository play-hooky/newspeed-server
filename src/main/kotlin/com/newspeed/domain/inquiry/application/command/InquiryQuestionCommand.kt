package com.newspeed.domain.inquiry.application.command

import com.newspeed.domain.inquiry.domain.InquiryQuestion
import com.newspeed.domain.user.domain.User
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Positive
import javax.validation.constraints.Size

data class InquiryQuestionCommand(
    @field:Positive(message = "사용자를 식별할 수 없습니다.")
    val userId: Long,

    @field:NotBlank(message = "[title] 문의 내용 제목을 입력해주세요.")
    val title: String,

    @field:NotBlank(message = "[body] 문의 내용 본문을 입력해주세요.")
    @field:Size(max = 50000, message = "[body] 문의 내용 본문 길이 제한을 초과하였습니다.")
    val body: String
) {
    fun toEntity(
        user: User
    ): InquiryQuestion = InquiryQuestion(
        user = user,
        title = title,
        body = body
    )
}
