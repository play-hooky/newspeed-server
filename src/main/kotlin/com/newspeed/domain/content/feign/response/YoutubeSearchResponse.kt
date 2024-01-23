package com.newspeed.domain.content.feign.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.newspeed.domain.content.config.YoutubeConfigProperties
import com.newspeed.domain.content.converter.toYouTubeTimeDifference
import com.newspeed.domain.content.converter.toYoutubeView
import com.newspeed.domain.content.domain.enums.QueryPlatform
import com.newspeed.domain.content.dto.ContentHostDTO
import com.newspeed.domain.content.dto.ContentResponseDTO
import com.newspeed.domain.content.dto.ContentYoutubeDTO
import com.newspeed.domain.content.feign.dto.YoutubeIDDTO
import com.newspeed.domain.content.feign.dto.YoutubePageDTO
import com.newspeed.domain.content.feign.dto.YoutubeVideoSnippetDTO
import com.newspeed.domain.content.feign.enums.YoutubeQueryParts
import com.newspeed.domain.content.feign.request.YoutubeChannelRequest
import com.newspeed.domain.content.feign.request.YoutubeVideoDetailRequest
import com.newspeed.domain.content.feign.request.toYoutubeContentIds
import com.newspeed.domain.search.api.response.ContentSearchResponse

@JsonIgnoreProperties(ignoreUnknown = true)
data class YoutubeSearchResponse(
    val kind: String,
    val etag: String,
    val nextPageToken: String,
    val regionCode: String,
    val pageInfo: YoutubePageDTO,
    val items: List<Item>
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
     data class Item(
        val kind: String,
        val etag: String,
        val id: YoutubeIDDTO,
        val snippet: YoutubeVideoSnippetDTO
     ) {

         fun toContent(
             channel: YoutubeChannelResponse.Item,
             video: YoutubeVideoDetailResponse.Item,
             videoUrl: String
         ): ContentResponseDTO = ContentResponseDTO(
             platform = QueryPlatform.YOUTUBE,
             host = ContentHostDTO(
                 profileImgUrl = channel.thumbnailUrl(),
                 nickname = this.snippet.channelTitle
             ),
             youtube = ContentYoutubeDTO(
                 id = id.videoId,
                 thumbnailUrl = snippet.thumbnails.high.url,
                 title = snippet.title,
                 url = videoUrl + id.videoId,
                 views = video.statistics.viewCount.toYoutubeView(),
                 todayBefore = this.snippet.publishedAt.toYouTubeTimeDifference()
             ),
             instagram = null
         )
     }

    fun with(
        channelResponse: YoutubeChannelResponse,
        videoDetailResponse: YoutubeVideoDetailResponse,
        videoUrl: String
    ): ContentSearchResponse = ContentSearchResponse(
        contents = items
            .map { it.toContent(
                channelResponse.findById(it.snippet.channelId),
                videoDetailResponse.findById(it.id.videoId),
                videoUrl
            ) }
    )

    fun toChannelRequest(
        youtubeConfigProperties: YoutubeConfigProperties
    ): YoutubeChannelRequest = YoutubeChannelRequest(
        key = youtubeConfigProperties.key,
        part = YoutubeQueryParts.snippet.name,
        id = getChannelIds().toYoutubeContentIds()
    )

    private fun getChannelIds(): List<String> = items
        .map { it.snippet.channelId }

    fun toVideoDetailRequest(
        youtubeConfigProperties: YoutubeConfigProperties
    ): YoutubeVideoDetailRequest = YoutubeVideoDetailRequest(
        part = "${YoutubeQueryParts.snippet.name},${YoutubeQueryParts.statistics.name}",
        key = youtubeConfigProperties.key,
        id = getVideoIds().toYoutubeContentIds()
    )

    private fun getVideoIds(): List<String> = items
        .map { it.id.videoId }
}

