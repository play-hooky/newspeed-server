package com.newspeed.global.enums

import java.sql.Time
import java.text.SimpleDateFormat

enum class DateTimeFormats(
    val value: SimpleDateFormat
) {
    HOUR_MINUTE_TIME(SimpleDateFormat("HH:mm")),
    ;
}

fun String.toTime(): Time = Time(DateTimeFormats.HOUR_MINUTE_TIME.value.parse(this).time)