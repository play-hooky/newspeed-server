package com.newspeed.domain.content.dto

import com.newspeed.domain.content.domain.enums.QueryPlatform

data class ContentResponseDTO(
    val platform: QueryPlatform,
    val host: ContentHostDTO,
    val youtube: ContentYoutubeDTO?,
    val instagram: ContentInstagramDTO?
)
