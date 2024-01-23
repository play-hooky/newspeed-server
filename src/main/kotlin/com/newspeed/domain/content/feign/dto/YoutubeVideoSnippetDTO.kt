package com.newspeed.domain.content.feign.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.time.LocalDateTime

@JsonIgnoreProperties(ignoreUnknown = true)
data class YoutubeVideoSnippetDTO(
    val publishedAt: LocalDateTime,
    val channelId: String,
    val title: String,
    val description: String,
    val thumbnails: YoutubeThumbnailDTO,
    val channelTitle: String,
    val liveBroadcastContent: String
)