package com.newspeed.domain.content.converter

import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun LocalDateTime.toYouTubeTimeDifference(): String {
    val duration = Duration.between(this, LocalDateTime.now())

    with(duration) {
        return when {
            seconds < 60 -> "${seconds}초 전"
            toMinutes() < 60 -> "${toMinutes()}분 전"
            toHours() < 24 -> "${toHours()}시간 전"
            toDays() < 30 -> "${toDays()}일 전"
            toDays() < 365 -> "${toDays() / 30}개월 전"
            toDays() < 365 * 100 -> "${toDays() / 365}년 전"
            else -> this@toYouTubeTimeDifference.format(DateTimeFormatter.ofPattern("yyyy년 M월 d일"))
        }
    }
}

fun String?.toYoutubeView(): String {
    val views = this?.toLong() ?: 0L

    return when {
        views < 1000 -> "${views}회"
        (views < 10000) and (views % 1000 == 0L) -> "${views / 1000}천회"
        (views < 10000) and (views % 1000 != 0L) -> String.format("%.1f천회", views / 1000.0)
        views < 100000000 -> "${views / 10000}만회"
        views < 1000000000000 -> "${views / 100000000}억회"
        else -> "${views}회"
    }
}