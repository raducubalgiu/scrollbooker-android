package com.example.scrollbooker.core.extensions

import org.threeten.bp.LocalTime

fun LocalTime.toPrettyTime(): String = this.format(DateTimeFormatters.uiTime)