package com.newspeed.domain.content.application

import com.newspeed.domain.content.config.YoutubeConfigProperties
import com.newspeed.domain.content.domain.enums.QueryPlatform
import com.newspeed.domain.content.dto.ContentResponseDTO
import com.newspeed.domain.content.feign.YoutubeClient
import com.newspeed.domain.content.feign.request.toYoutubeContentIds
import com.newspeed.domain.content.feign.response.YoutubeChannelResponse
import com.newspeed.domain.content.feign.response.YoutubeSearchResponse
import com.newspeed.domain.content.feign.response.YoutubeVideoDetailResponse
import com.newspeed.domain.search.api.request.ContentSearchRequest
import org.springframework.stereotype.Service

@Service
class YoutubeSearchService(
    private val youtubeClient: YoutubeClient,
    private val youtubeConfigProperties: YoutubeConfigProperties
): ContentSearchClient {
    override fun getQueryPlatform(): QueryPlatform = QueryPlatform.YOUTUBE

    override fun search(
        request: ContentSearchRequest
    ): List<ContentResponseDTO> {
        val searchResponse = searchVideo(request)

        return search(searchResponse.videoIDs())
    }

    override fun search(
        ids: List<String>
    ): List<ContentResponseDTO> {
        val videoDetailResponse = searchVideoDetail(ids)
        val channelResponse = searchChannel(videoDetailResponse)

        return channelResponse
            .with(videoDetailResponse, youtubeConfigProperties.videoUrl)
    }

    private fun searchVideo(
        contentSearchRequest: ContentSearchRequest
    ): YoutubeSearchResponse {
        val request = contentSearchRequest.toYoutubeSearchRequest(youtubeConfigProperties)
        return youtubeClient.search(request)
    }

    private fun searchVideoDetail(
        ids: List<String>
    ): YoutubeVideoDetailResponse {
        val videoDetailRequest = youtubeConfigProperties.toYoutubeVideoDetailRequest(ids.toYoutubeContentIds())
        return youtubeClient.getDetailContent(videoDetailRequest)
    }

    private fun searchChannel(
        videoDetailResponse: YoutubeVideoDetailResponse
    ): YoutubeChannelResponse {
        val channelRequest = youtubeConfigProperties.toYoutubeChannelRequest(videoDetailResponse.channelIds())
        return youtubeClient.getChannel(channelRequest)
    }
}