package com.newspeed.domain.content.feign.request

import feign.Param

data class InstagramHashTagIDRequest(
    val accessToken: String,
    val query: String,
    val instagramUserId: String
) {
    @JvmName("get_access_token")
    @Param("access_token")
    fun getAccessToken(): String = accessToken

    @JvmName("get_query")
    @Param("q")
    fun getQuery(): String = query

    @JvmName("get_user_id")
    @Param("user_id")
    fun getInstagramUserId(): String = instagramUserId
}
