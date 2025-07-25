package com.example.scrollbooker.core.util

import android.annotation.SuppressLint

@SuppressLint("DefaultLocale")
fun formatDistance(distanceKm: Float): String {
    return when {
        distanceKm < 0.5 -> "<500m"
        distanceKm < 1.0 -> "${(distanceKm * 1000).toInt()}m"
        distanceKm % 1.0 < 0.05 -> "${distanceKm.toInt()}km"
        else -> String.format("%.1fkm", distanceKm)
    }
}