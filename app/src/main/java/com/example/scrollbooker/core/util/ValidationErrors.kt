package com.example.scrollbooker.core.util
import android.content.Context
import com.example.scrollbooker.R

fun checkLength(context: Context, field: String, minLength: Int = 0, maxLength: Int): String? {
    val trimmed = field.trim()

    return when {
        trimmed.length < minLength -> context.getString(R.string.minLengthValidationMessage, minLength)
        trimmed.length > maxLength -> context.getString(R.string.maxLengthValidationMessage, maxLength)
        else -> null
    }
}