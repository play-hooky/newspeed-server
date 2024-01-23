package com.newspeed.domain.content.dto

data class ContentYoutubeDTO(
    val id: String,
    val thumbnailUrl: String,
    val title: String,
    val url: String,
    val views: String,
    val todayBefore: String
)