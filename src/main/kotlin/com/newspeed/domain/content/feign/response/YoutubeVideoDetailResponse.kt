package com.newspeed.domain.content.feign.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.newspeed.domain.content.converter.toYouTubeTimeDifference
import com.newspeed.domain.content.converter.toYoutubeView
import com.newspeed.domain.content.dto.ContentYoutubeDTO
import com.newspeed.domain.content.feign.dto.YoutubeVideoSnippetDTO
import com.newspeed.domain.content.feign.dto.YoutubeVideoStatisticsDTO
import com.newspeed.domain.content.feign.request.toYoutubeContentIds

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
        val statistics: YoutubeVideoStatisticsDTO,
        val snippet: YoutubeVideoSnippetDTO
    )  {
        fun toContent(
            videoUrl: String
        ): ContentYoutubeDTO = ContentYoutubeDTO(
            id = id,
            thumbnailUrl = snippet.thumbnails.high.url,
            title = snippet.title,
            url = videoUrl + id,
            views = statistics.viewCount.toYoutubeView(),
            todayBefore = snippet.publishedAt.toYouTubeTimeDifference()
        )
    }

    fun findById(
        id: String
    ): Item = this.items
        .first { it.id == id }

    fun channelIds(): String = this.items
        .map { it.snippet.channelId }
        .toYoutubeContentIds()
}
