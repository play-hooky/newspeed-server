package com.newspeed.domain.content.application

import com.newspeed.domain.content.config.YoutubeConfigProperties
import com.newspeed.domain.content.domain.enums.QueryPlatform
import com.newspeed.domain.content.dto.ContentResponseDTO
import com.newspeed.domain.content.feign.YoutubeClient
import com.newspeed.domain.content.feign.request.toYoutubeContentIds
import com.newspeed.domain.search.api.request.ContentSearchRequest
import com.newspeed.domain.search.api.response.ContentSearchResponse
import org.springframework.stereotype.Service

@Service
class YoutubeSearchService(
    private val youtubeClient: YoutubeClient,
    private val youtubeConfigProperties: YoutubeConfigProperties
): ContentSearchClient {
    override fun getQueryPlatform(): QueryPlatform = QueryPlatform.YOUTUBE

    override fun search(
        request: ContentSearchRequest
    ): ContentSearchResponse {
        val searchRequest = request.toYoutubeSearchRequest(youtubeConfigProperties)
        val searchResponse = youtubeClient.search(searchRequest)

        val channelRequest = searchResponse.toChannelRequest(youtubeConfigProperties)
        val channelResponse = youtubeClient.getChannel(channelRequest)

        val videoDetailRequest = searchResponse.toVideoDetailRequest(youtubeConfigProperties)
        val videoDetailResponse = youtubeClient.getDetailContent(videoDetailRequest)

        return searchResponse
            .with(channelResponse, videoDetailResponse, youtubeConfigProperties.videoUrl)
    }

    override fun search(
        ids: List<String>
    ): List<ContentResponseDTO> {
        val videoDetailRequest = youtubeConfigProperties.toYoutubeVideoDetailRequest(ids.toYoutubeContentIds())
        val videoDetailResponse = youtubeClient.getDetailContent(videoDetailRequest)

        val channelRequest = youtubeConfigProperties.toYoutubeChannelRequest(videoDetailResponse.channelIds())
        val channelResponse = youtubeClient.getChannel(channelRequest)

        return channelResponse
            .with(videoDetailResponse, youtubeConfigProperties.videoUrl)
    }
}