package com.newspeed.domain.content.feign

import com.newspeed.domain.content.feign.request.InstagramHashTagIDRequest
import com.newspeed.domain.content.feign.request.InstagramMediaRequest
import com.newspeed.domain.content.feign.response.InstagramHashTagIDResponse
import com.newspeed.domain.content.feign.response.InstagramMediaResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.cloud.openfeign.SpringQueryMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@FeignClient(name = "instagram-client", url = "\${content.instagram.url}")
interface InstagramClient {

    @GetMapping("/v18.0/ig_hashtag_search")
    fun findHashTagID(
        @SpringQueryMap request: InstagramHashTagIDRequest
    ): InstagramHashTagIDResponse

    @GetMapping("/{hashTagID}/top_media")
    fun findTopMedias(
        @PathVariable hashTagID: String,
        @SpringQueryMap request: InstagramMediaRequest
    ): InstagramMediaResponse

    @GetMapping("/{hashTagID}/recent_media")
    fun findRecentMedias(
        @PathVariable hashTagID: String,
        @SpringQueryMap request: InstagramMediaRequest
    ): InstagramMediaResponse
}