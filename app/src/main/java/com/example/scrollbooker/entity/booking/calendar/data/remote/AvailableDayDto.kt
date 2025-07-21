package com.example.scrollbooker.entity.booking.calendar.data.remote

import com.google.gson.annotations.SerializedName

data class AvailableDayDto(
    @SerializedName("is_closed")
    val isClosed: Boolean,

    @SerializedName("available_slots")
    val availableSlots: List<SlotDto>
)

data class SlotDto(
    @SerializedName("start_date_utc")
    val startDateUtc: String,

    @SerializedName("end_date_utc")
    val endDateUtc: String,

    @SerializedName("start_date_locale")
    val startDateLocale: String,

    @SerializedName("end_date_locale")
    val endDateLocale: String,
)