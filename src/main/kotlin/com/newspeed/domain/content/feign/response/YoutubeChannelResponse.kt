package com.newspeed.domain.content.feign.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.newspeed.domain.content.domain.enums.QueryPlatform
import com.newspeed.domain.content.dto.ContentHostDTO
import com.newspeed.domain.content.dto.ContentResponseDTO
import com.newspeed.domain.content.feign.dto.YoutubeChannelSnippetDTO
import com.newspeed.domain.content.feign.dto.YoutubePageDTO

@JsonIgnoreProperties(ignoreUnknown = true)
data class YoutubeChannelResponse(
    val kind: String,
    val etag: String,
    val pageInfo: YoutubePageDTO,
    val items: List<Item>
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Item(
        val kind: String,
        val etag: String,
        val id: String,
        val snippet: YoutubeChannelSnippetDTO
    ) {
        fun thumbnailUrl(): String = this.snippet.thumbnailUrl()
    }

    fun with(
        videoDetailResponse: YoutubeVideoDetailResponse,
        videoUrl: String
    ): List<ContentResponseDTO> = videoDetailResponse
        .items
        .map { ContentResponseDTO(
            platform = QueryPlatform.YOUTUBE,
            host = ContentHostDTO(
                profileImgUrl = this.findById(it.snippet.channelId).thumbnailUrl(),
                nickname = it.snippet.channelTitle
            ),
            youtube = it.toContent(videoUrl),
            instagram = null
        ) }

    private fun findById(
        id: String
    ): Item = items
        .first { it.id == id }
}