package com.newspeed.domain.content.feign.enums

enum class InstagramFields {
    caption,
    comments_count,
    id,
    like_count,
    media_type,
    media_url,
    permalink,
    timestamp
    ;

    companion object {
        override fun toString(): String = values()
            .joinToString(separator = ",") { it.name }
    }
}