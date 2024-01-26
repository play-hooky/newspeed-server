package com.newspeed.domain.auth.application.command

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Positive

data class WithdrawalCommand(
    @field:Positive(message = "사용자를 식별할 수 없습니다.")
    val userId: Long,

    @field:NotBlank(message = "authorization code를 입력해주세요.")
    val authorizationCode: String,
)
