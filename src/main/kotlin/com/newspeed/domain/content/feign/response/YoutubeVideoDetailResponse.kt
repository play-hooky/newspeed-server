package com.newspeed.domain.content.feign.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class YoutubeVideoDetailResponse(
    val kind: String,
    val etag: String,
    val items: List<Item>
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Item(
        val kind: String,
        val etag: String,
        val id: String,
        val statistics: Statistics
    ) {
        @JsonIgnoreProperties(ignoreUnknown = true)
        data class Statistics(
            val viewCount: String,
            val likeCount: String,
            val favoriteCount: String,
            val commentCount: String
        )
    }

    fun findById(
        id: String
    ): Item = this.items
        .first { it.id == id }
}
