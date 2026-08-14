package com.example.scrollbooker.core.extensions
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter
import timber.log.Timber
import java.math.BigDecimal
import java.text.Normalizer

fun String.toLocalTimeOrNull(): LocalTime? {
    return try {
        val inputFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")
        LocalTime.parse(this, inputFormatter)

    } catch (e: Exception) {
        Timber.e("ERROR on Parsing DateTime from String: $e")
        null
    }
}

fun String.toBigDecimalOrNull(): BigDecimal? =
    try { BigDecimal(this.replace(",", ".")) } catch (e: NumberFormatException) { null }

fun String.toSlug(): String {
    val normalized = Normalizer.normalize(this, Normalizer.Form.NFD)
        .replace(Regex("\\p{Mn}+"), "")

    return normalized
        .trim()
        .lowercase()
        .replace(Regex("[^a-z0-9\\s-]"), "")
        .replace(Regex("\\s+"), "-")
        .replace(Regex("-+"), "-")
        .trim('-')
}
