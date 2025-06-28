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