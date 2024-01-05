package com.newspeed.domain.content.domain.enums

enum class QueryPlatform {
    INSTAGRAM,
    YOUTUBE,
    NEWSPEED
    ;

    companion object {
        fun findByUrl(
            url: String
        ): QueryPlatform = values()
            .find { url.contains(it.name.lowercase()) }
            ?: NEWSPEED
    }
}