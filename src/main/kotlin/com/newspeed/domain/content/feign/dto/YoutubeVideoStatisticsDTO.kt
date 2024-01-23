package com.newspeed.domain.content.feign.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class YoutubeVideoStatisticsDTO(
    val viewCount: String,
    val likeCount: String,
    val favoriteCount: String,
    val commentCount: String
)
