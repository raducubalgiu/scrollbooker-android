package com.example.scrollbooker.ui.appointments.sheets

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RatingReviewUpdate(
    val rating: Int,
    val review: String?
): Parcelable