package com.newspeed.domain.content.feign.request

data class YoutubeVideoDetailRequest(
    val part: String,
    val key: String,
    val id: String
)

fun List<String>.toYoutubeContentIds() = this.joinToString(separator = ",")