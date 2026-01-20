package com.example.scrollbooker.core.util
import android.content.Context
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import timber.log.Timber
import java.math.BigDecimal

fun checkLength(context: Context, field: String, minLength: Int = 0, maxLength: Int): String? {
    val trimmed = field.trim()

    return when {
        trimmed.length < minLength -> context.getString(R.string.minLengthValidationMessage, minLength)
        trimmed.length >= maxLength -> context.getString(R.string.maxLengthValidationMessage, maxLength)
        else -> null
    }
}

fun checkMinMax(context: Context, field: String, min: Int? = null, max: Int? = null): String? {
    if(field.isEmpty()) return context.getString(R.string.requiredValidationMessage)
    
    val value = field.trim().toIntOrNull() ?: return null

    return when {
        min != null && value < min -> context.getString(R.string.minNumberValidationMessage, min)
        max !== null &&  value >= max -> context.getString(R.string.maxNumberValidationMessage, max)
        else -> null
    }
}

fun checkRequired(context: Context, value: String?): String? {
    val trimmed = value?.trim().orEmpty()

    return if (trimmed.isEmpty() || trimmed.equals("null", ignoreCase = true)) {
        context.getString(R.string.requiredValidationMessage)
    } else null
}

fun checkEmail(email: String): String? {
    return when {
        email.isBlank() -> "Emailul nu poate fi gol"
        !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
            "Te rugam sa introduci un email valid"
        else -> null
    }
}

fun checkPassword(password: String): Boolean {
    return when {
        password.isBlank() -> false
        password.length < 8 -> false
        password.length > 20 -> false
        !password.any { it.isUpperCase() } -> false
        !password.any { it.isDigit() } -> false
        else -> true
    }
}