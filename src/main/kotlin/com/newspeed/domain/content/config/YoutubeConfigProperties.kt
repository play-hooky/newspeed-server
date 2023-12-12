package com.newspeed.domain.content.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
data class YoutubeConfigProperties(
    @Value("\${content.youtube.key}") val key: String
)