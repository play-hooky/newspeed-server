package com.newspeed.domain.auth.feign.response

import com.fasterxml.jackson.annotation.JsonProperty

data class AppleOAuth2TokenHeader(
    @JsonProperty("kid")
    val keyId: String,

    @JsonProperty("alg")
    val algorithm: String
)
