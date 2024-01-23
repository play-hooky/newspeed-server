package com.newspeed.domain.content.application.command

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Positive

data class ContentDeleteCommand(
    @field:Positive(message = "사용자를 식별할 수 없습니다.")
    val userId: Long,
    @field:NotBlank(message = "url을 입력해주세요.")
    val url: String
)