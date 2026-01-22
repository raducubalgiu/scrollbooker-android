package com.example.scrollbooker.core.snackbar

import android.content.Context
import androidx.annotation.StringRes

sealed interface UiText {
    data class Dynamic(val value: String) : UiText

    data class Resource(
        @StringRes val resId: Int,
        val args: List<Any> = emptyList()
    ) : UiText
}

fun UiText.asString(context: Context): String = when (this) {
    is UiText.Dynamic -> value
    is UiText.Resource -> {
        if (args.isEmpty()) context.getString(resId)
        else context.getString(resId, *args.toTypedArray())
    }
}