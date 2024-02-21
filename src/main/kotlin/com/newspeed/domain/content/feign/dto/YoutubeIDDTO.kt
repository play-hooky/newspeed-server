package com.newspeed.domain.content.feign.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class YoutubeIDDTO(
    val kind: String,
    val videoId: String?
)