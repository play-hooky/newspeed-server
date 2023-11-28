package com.newspeed.domain.auth.api.request

import com.fasterxml.jackson.annotation.JsonIgnore
import com.newspeed.domain.auth.domain.LoginPlatform
import javax.validation.constraints.NotBlank

data class KakaoLoginRequest(
    @field:NotBlank(message = "authorization code를 입력해주세요.")
    val authorizationCode: String,

    @JsonIgnore
    val loginPlatform: LoginPlatform = LoginPlatform.KAKAO
)
