package com.newspeed.domain.content.application

import com.newspeed.domain.content.api.request.ContentSearchRequest
import com.newspeed.domain.content.api.response.ContentSearchResponse
import com.newspeed.domain.content.config.YoutubeConfigProperties
import com.newspeed.domain.content.domain.enums.QueryPlatform
import com.newspeed.domain.content.feign.YoutubeClient
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
            .toContentSearchResponse(channelResponse, videoDetailResponse)
    }
}