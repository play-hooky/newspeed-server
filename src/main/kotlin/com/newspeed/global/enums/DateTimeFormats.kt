package com.newspeed.global.enums

import java.sql.Date
import java.sql.Time
import java.text.SimpleDateFormat

enum class DateTimeFormats(
    val value: SimpleDateFormat
) {
    HOUR_MINUTE_TIME(SimpleDateFormat("HH:mm")),
    ;
}

fun String.toTime(): Time = Time(DateTimeFormats.HOUR_MINUTE_TIME.value.parse(this).time)

fun Time.toStringWithoutSeconds(): String = DateTimeFormats.HOUR_MINUTE_TIME.value.format(Date(this.time))