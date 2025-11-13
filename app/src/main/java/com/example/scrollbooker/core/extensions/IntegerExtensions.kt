package com.example.scrollbooker.core.extensions

fun Int.formatDuration(padMinutes: Boolean = false): String {
    val total = this.coerceAtLeast(0)
    val hours = total / 60
    val minutes = total % 60

    return if (hours == 0) {
        "${minutes}min"
    } else {
        if(minutes == 0) {
            "${hours}h"
        } else {
            val mins = if(padMinutes && minutes < 10) "0${minutes}" else "$minutes"
            "${hours}h ${mins}min"
        }
    }
}