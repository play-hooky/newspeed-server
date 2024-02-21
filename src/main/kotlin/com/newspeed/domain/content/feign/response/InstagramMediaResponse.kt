package com.newspeed.domain.content.feign.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.newspeed.domain.content.domain.enums.QueryPlatform
import com.newspeed.domain.content.dto.ContentHostDTO
import com.newspeed.domain.content.dto.ContentInstagramDTO
import com.newspeed.domain.content.dto.ContentResponseDTO

@JsonIgnoreProperties(ignoreUnknown = true)
data class InstagramMediaResponse(
    val data: List<Data>,
    val paging: Paging
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Data(
        val id: String,
        val caption: String,
        @JsonProperty("comments_count")
        val commentsCount: Long,
        @JsonProperty("like_count")
        val likeCount: Long,
        @JsonProperty("media_type")
        val mediaType: String,
        @JsonProperty("media_url")
        val mediaUrl: String?,
        val permalink: String,
        val timestamp: String
    ) {
        fun toContentResponseDTO(): ContentResponseDTO = ContentResponseDTO(
            platform = QueryPlatform.INSTAGRAM,
            host = ContentHostDTO(
                profileImgUrl = mediaUrl ?: "https://www.newspeed.store/default-image",
                nickname = body()
            ),
            youtube = null,
            instagram = ContentInstagramDTO(
                id = id,
                imgUrls = if (mediaUrl == null) emptyList() else listOf(mediaUrl),
                title = body(),
                body = body(),
                url = permalink,
                hashTags = hashTags()
            )
        )

        private fun body(): String {
            val titleWithHashTags = caption.split("#")

            return titleWithHashTags.subList(0, 1)
                .firstOrNull()
                ?: caption

        }

        private fun hashTags(): List<String> {
            val titleWithHashTags = caption.split("#")

            return titleWithHashTags.subList(1, titleWithHashTags.size)
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Paging(
        val cursors: Cursors,
        val next: String
    ) {
        @JsonIgnoreProperties(ignoreUnknown = true)
        data class Cursors(
            val after: String
        )
    }

    fun toContentResponseDTOs(): List<ContentResponseDTO> = this.data
        .map { it.toContentResponseDTO() }
}
