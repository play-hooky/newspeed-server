package com.newspeed.domain.content.feign.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class YoutubeThumbnailDTO(
    val default: Thumbnail,
    val medium: Thumbnail,
    val high: Thumbnail
) {
    data class Thumbnail(
        val url: String,
        val width: Int,
        val height: Int
    )
}
