package com.newspeed.domain.content.feign.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.time.LocalDateTime

@JsonIgnoreProperties(ignoreUnknown = true)
data class YoutubeChannelSnippetDTO(
    val title: String,
    val description: String,
    val publishedAt: LocalDateTime,
    val thumbnails: YoutubeThumbnailDTO,
    val localized: Localized
) {
    data class Localized(
        val title: String,
        val description: String
    )

    fun thumbnailUrl(): String = thumbnails.high.url
}