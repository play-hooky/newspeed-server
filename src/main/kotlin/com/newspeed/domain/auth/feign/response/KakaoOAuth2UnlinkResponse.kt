package com.newspeed.domain.auth.feign.response

import com.fasterxml.jackson.annotation.JsonProperty

data class KakaoOAuth2UnlinkResponse(
    @JsonProperty("id")
    val kakaoUserId: Long
)
