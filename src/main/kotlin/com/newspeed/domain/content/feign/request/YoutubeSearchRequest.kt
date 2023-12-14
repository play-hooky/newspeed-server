package com.newspeed.domain.content.feign.request

import com.newspeed.domain.content.feign.enums.YoutubeSearchOrder

data class YoutubeSearchRequest(
    val part: String,
    val key: String,
    val q: String?,
    val maxResults: Int,
    val order: YoutubeSearchOrder,
//    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
//    val publishedAfter: LocalDateTime
)