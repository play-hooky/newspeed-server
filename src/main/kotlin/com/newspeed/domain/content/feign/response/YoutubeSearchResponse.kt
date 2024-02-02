package com.newspeed.domain.content.feign.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.newspeed.domain.content.feign.dto.YoutubeIDDTO
import com.newspeed.domain.content.feign.dto.YoutubePageDTO
import com.newspeed.domain.content.feign.dto.YoutubeVideoSnippetDTO

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
     )

    fun videoIDs(): List<String> = items
        .map { it.id.videoId ?: "" }
}

