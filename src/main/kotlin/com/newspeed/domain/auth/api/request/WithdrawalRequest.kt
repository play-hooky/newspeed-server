package com.newspeed.domain.auth.api.request

import com.newspeed.domain.auth.application.command.WithdrawalCommand
import javax.validation.constraints.NotBlank

data class WithdrawalRequest(
    @field:NotBlank(message = "authorization code를 입력해주세요.")
    val authorizationCode: String
) {
    fun toCommand(
        userId: Long
    ): WithdrawalCommand = WithdrawalCommand(
        userId = userId,
        authorizationCode = authorizationCode
    )
}
