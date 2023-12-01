package com.newspeed.domain.auth.feign.response

import com.fasterxml.jackson.annotation.JsonProperty

data class AppleOAuth2PublicKeyResponse(
    val keys: List<Key>
) {

    data class Key(
        @JsonProperty("kty")
        val keyType: String,

        @JsonProperty("kid")
        val keyId: String,

        @JsonProperty("use")
        val usage: String,

        @JsonProperty("alg")
        val algorithm: String,

        @JsonProperty("n")
        val modulus: String,

        @JsonProperty("e")
        val exponent: String
    )
}