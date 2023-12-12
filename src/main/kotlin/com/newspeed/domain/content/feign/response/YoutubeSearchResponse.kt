package com.newspeed.domain.content.feign.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.newspeed.domain.content.api.response.ContentSearchResponse
import com.newspeed.domain.content.config.YoutubeConfigProperties
import com.newspeed.domain.content.domain.enums.QueryPlatform
import com.newspeed.domain.content.feign.enums.YoutubeQueryParts
import com.newspeed.domain.content.feign.request.YoutubeChannelRequest
import com.newspeed.domain.content.feign.request.YoutubeVideoDetailRequest
import com.newspeed.domain.content.converter.toYouTubeTimeDifference
import com.newspeed.domain.content.converter.toYoutubeView
import java.time.LocalDateTime

@JsonIgnoreProperties(ignoreUnknown = true)
data class YoutubeSearchResponse(
    val kind: String,
    val etag: String,
    val nextPageToken: String,
    val regionCode: String,
    val pageInfo: Page,
    val items: List<Item>
) {

    data class Page(
        val totalResults: Int,
        val resultsPerPage: Int
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
     data class Item(
         val kind: String,
         val etag: String,
         val id: Id,
         val snippet: Snippet
     ) {
         data class Id(
             val kind: String,
             val videoId: String
         )

        @JsonIgnoreProperties(ignoreUnknown = true)
         data class Snippet(
             val publishedAt: LocalDateTime,
             val channelId: String,
             val title: String,
             val description: String,
             val thumbnails: Thumbnails,
             val channelTitle: String,
             val liveBroadcastContent: String,
             val publishTime: LocalDateTime
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
         }

         fun toContent(
             channel: YoutubeChannelResponse.Item,
             video: YoutubeVideoDetailResponse.Item
         ): ContentSearchResponse.Content = ContentSearchResponse.Content(
             platform = QueryPlatform.YOUTUBE,
             host = ContentSearchResponse.Content.Host(
                 profileImgUrl = channel.snippet.thumbnails.high.url,
                 nickname = this.snippet.channelTitle
             ),
             youtube = ContentSearchResponse.Content.Youtube(
                 thumbnailUrl = snippet.thumbnails.high.url,
                 title = snippet.title,
                 url = "https://www.youtube.com/watch?v=${id.videoId}",
                 view = video.statistics.viewCount.toYoutubeView(),
                 todayBefore = this.snippet.publishTime.toYouTubeTimeDifference()
             ),
             instagram = null
         )
     }

    fun toContentSearchResponse(
        channelResponse: YoutubeChannelResponse,
        videoDetailResponse: YoutubeVideoDetailResponse
    ): ContentSearchResponse = ContentSearchResponse(
        contents = items
            .map { it.toContent(
                channelResponse.findById(it.snippet.channelId),
                videoDetailResponse.findById(it.id.videoId)
            ) }
    )

    fun toChannelRequest(
        youtubeConfigProperties: YoutubeConfigProperties
    ): YoutubeChannelRequest = YoutubeChannelRequest(
        key = youtubeConfigProperties.key,
        part = YoutubeQueryParts.snippet.name,
        id = getChannelIds().joinToString(",")
    )

    private fun getChannelIds(): List<String> = items
        .map { it.snippet.channelId }

    fun toVideoDetailRequest(
        youtubeConfigProperties: YoutubeConfigProperties
    ): YoutubeVideoDetailRequest = YoutubeVideoDetailRequest(
        part = YoutubeQueryParts.statistics.name,
        key = youtubeConfigProperties.key,
        id = getVideoIds().joinToString(",")
    )

    private fun getVideoIds(): List<String> = items
        .map { it.id.videoId }
}

