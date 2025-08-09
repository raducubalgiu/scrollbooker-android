package com.example.scrollbooker.ui.sharedModules.posts.util

import android.annotation.SuppressLint

@SuppressLint("DefaultLocale")
fun formatCounters(count: Int): String {
    return when {
        count < 1_000 -> count.toString()

        count < 10_000 -> String.format("%,d", count)

        else -> "1M+"
    }
}