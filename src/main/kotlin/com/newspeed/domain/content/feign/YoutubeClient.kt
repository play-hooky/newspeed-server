package com.newspeed.domain.content.feign

import com.newspeed.domain.content.feign.request.YoutubeChannelRequest
import com.newspeed.domain.content.feign.request.YoutubeSearchRequest
import com.newspeed.domain.content.feign.request.YoutubeVideoDetailRequest
import com.newspeed.domain.content.feign.response.YoutubeChannelResponse
import com.newspeed.domain.content.feign.response.YoutubeSearchResponse
import com.newspeed.domain.content.feign.response.YoutubeVideoDetailResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.cloud.openfeign.SpringQueryMap
import org.springframework.web.bind.annotation.GetMapping

@FeignClient(name = "youtube-client", url = "\${content.youtube.url}")
interface YoutubeClient {

    @GetMapping("/youtube/v3/search")
    fun search(
        @SpringQueryMap request: YoutubeSearchRequest
    ): YoutubeSearchResponse

    @GetMapping("/youtube/v3/channels")
    fun getChannel(
        @SpringQueryMap request: YoutubeChannelRequest
    ): YoutubeChannelResponse

    @GetMapping("/youtube/v3/videos")
    fun getDetailContent(
        @SpringQueryMap request: YoutubeVideoDetailRequest
    ): YoutubeVideoDetailResponse
}