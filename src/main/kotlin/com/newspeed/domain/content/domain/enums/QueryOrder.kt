package com.newspeed.domain.content.domain.enums

import com.newspeed.domain.content.feign.enums.YoutubeSearchOrder

enum class QueryOrder {
    views,
    date,
    ;

    fun isDate(): Boolean = this == date

    fun toYoutubeSearchOrder(): YoutubeSearchOrder = when(this) {
        views -> YoutubeSearchOrder.viewCount
        date -> YoutubeSearchOrder.date
    }
}