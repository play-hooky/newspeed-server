package com.newspeed.domain.content.feign.request

data class YoutubeChannelRequest(
    val key: String,
    val part: String,
    val id: String
)