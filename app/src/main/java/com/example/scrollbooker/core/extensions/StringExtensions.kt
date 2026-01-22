package com.example.scrollbooker.core.extensions
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter
import timber.log.Timber

fun String.toLocalTimeOrNull(): LocalTime? {
    return try {
        val inputFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")
        LocalTime.parse(this, inputFormatter)

    } catch (e: Exception) {
        Timber.e("ERROR on Parsing DateTime from String: $e")
        null
    }
}
