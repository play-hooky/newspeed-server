package com.newspeed.user.domain.auth.feign.request

import feign.Param

data class KakaoOAuth2TokenRequest(
    val grantType: String,
    val clientId: String,
    val redirectUri: String,
    val authorizationCode: String
) {
    @JvmName("get_grant_type")
    @Param("grant_type")
    fun getGrantType(): String {
        return grantType
    }

    @JvmName("get_client_id")
    @Param("client_id")
    fun getClientId(): String {
        return clientId
    }

    @JvmName("get_redirect_uri")
    @Param("redirect_uri")
    fun getRedirectUri(): String {
        return redirectUri
    }

    @JvmName("get_authorization_code")
    @Param("code")
    fun getAuthorizationCode(): String {
        return authorizationCode
    }
}
