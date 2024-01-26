package com.newspeed.domain.auth.feign.enums

enum class AppleTokenTypeHint(
    val value: String
) {
    REFRESH_TOKEN("refresh_token"),
    ACCESS_TOKEN("access_token"),
    ;
}