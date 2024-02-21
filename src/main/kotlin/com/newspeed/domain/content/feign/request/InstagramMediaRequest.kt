package com.newspeed.domain.content.feign.request

import feign.Param

data class InstagramMediaRequest(
    val instagramUserId: String,
    val fields: String,
    val accessToken: String,
    val limit: Int
) {
    @JvmName("get_access_token")
    @Param("access_token")
    fun getAccessToken(): String = accessToken

    @JvmName("get_user_id")
    @Param("user_id")
    fun getInstagramUserId(): String = instagramUserId
}
