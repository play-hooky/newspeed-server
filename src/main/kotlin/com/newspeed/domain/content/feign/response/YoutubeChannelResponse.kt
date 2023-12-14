package com.newspeed.domain.content.feign.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.time.LocalDateTime

@JsonIgnoreProperties(ignoreUnknown = true)
data class YoutubeChannelResponse(
    val kind: String,
    val etag: String,
    val pageInfo: YoutubeSearchResponse.Page,
    val items: List<Item>
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Item(
        val kind: String,
        val etag: String,
        val id: String,
        val snippet: Snippet
    ) {
        @JsonIgnoreProperties(ignoreUnknown = true)
        data class Snippet(
            val title: String,
            val description: String,
            val publishedAt: LocalDateTime,
            val thumbnails: Thumbnails,
            val localized: Localized
        ) {
            data class Thumbnails(
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

            data class Localized(
                val title: String,
                val description: String
            )
        }
    }

    fun findById(
        id: String
    ): Item = items
        .first { it.id == id }
}