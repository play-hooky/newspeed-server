package com.newspeed.domain.content.config

import com.newspeed.domain.content.feign.enums.YoutubeQueryParts
import com.newspeed.domain.content.feign.request.YoutubeChannelRequest
import com.newspeed.domain.content.feign.request.YoutubeVideoDetailRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
data class YoutubeConfigProperties(
    @Value("\${content.youtube.key}") val key: String,
    @Value("\${content.youtube.videoUrl}") val videoUrl: String
) {
    fun toYoutubeVideoDetailRequest(
        id: String
    ): YoutubeVideoDetailRequest = YoutubeVideoDetailRequest(
        part = "${YoutubeQueryParts.snippet.name},${YoutubeQueryParts.statistics.name}",
        key = key,
        id = id
    )

    fun toYoutubeChannelRequest(
        id: String
    ): YoutubeChannelRequest = YoutubeChannelRequest(
        part = YoutubeQueryParts.snippet.name,
        key = key,
        id = id
    )
}