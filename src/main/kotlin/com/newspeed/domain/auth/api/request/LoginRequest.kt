package com.newspeed.domain.auth.api.request

import com.newspeed.domain.auth.domain.LoginPlatform
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class LoginRequest(
    @field:NotBlank(message = "authorization code를 입력해주세요.")
    val authorizationCode: String,

    @field:NotNull(message = "로그인 하려는 SNS 플랫폼을 입력해주세요.")
    val loginPlatform: LoginPlatform
)
