package com.example.scrollbooker.feature.myBusiness.data.remote.schedules

import com.google.gson.annotations.SerializedName

data class ScheduleDto (
    val id: Int,

    @SerializedName("day_of_week")
    val dayOfWeek: String,

    @SerializedName("user_id")
    val userId: Int,

    @SerializedName("business_id")
    val businessId: Int,

    @SerializedName("start_time")
    val startTime: String,

    @SerializedName("end_time")
    val endTime: String,

    @SerializedName("day_of_week")
    val dayWeekNumber: Int
)