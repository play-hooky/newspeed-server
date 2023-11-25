package com.newspeed.user.domain.auth.api.request

import javax.validation.constraints.NotBlank

data class KakaoLoginRequest(
    @field:NotBlank(message = "authorization code를 입력해주세요.")
    val authorizationCode: String
)
