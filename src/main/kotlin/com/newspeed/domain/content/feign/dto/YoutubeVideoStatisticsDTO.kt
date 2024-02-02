package com.newspeed.domain.content.feign.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class YoutubeVideoStatisticsDTO(
    val view: String?,
    val like: String?,
    val favorite: String?,
    val comment: String?
) {
    val viewCount = view ?: "0"
    val likeCount = like ?: "0"
    val favoriteCount = favorite ?: "0"
    val commentCount = comment ?: "0"
}
