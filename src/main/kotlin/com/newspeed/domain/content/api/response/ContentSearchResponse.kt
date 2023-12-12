package com.newspeed.domain.content.api.response

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.newspeed.domain.content.application.serde.ContentSearchResponseSerializer
import com.newspeed.domain.content.domain.enums.QueryPlatform

@JsonSerialize(using = ContentSearchResponseSerializer::class)
data class ContentSearchResponse(
    val contents: List<Content>
) {
    data class Content(
        val platform: QueryPlatform,
        val host: Host,
        val youtube: Youtube?,
        val instagram: Instagram?
    ) {
        data class Host(
            val profileImgUrl: String,
            val nickname: String
        )

        data class Youtube(
            val thumbnailUrl: String,
            val title: String,
            val url: String,
            val view: String,
            val todayBefore: String
        )

        data class Instagram(
            val imgUrls: List<String>,
            val title: String,
            val body: String,
            val url: String,
            val hashTags: List<String>
        )
    }
}
