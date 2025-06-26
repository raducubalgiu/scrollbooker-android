package com.example.scrollbooker.entity.schedule.data.remote

import com.google.gson.annotations.SerializedName

data class ScheduleDto (
    val id: Int,

    @SerializedName("day_of_week")
    val dayOfWeek: String,

    @SerializedName("start_time")
    val startTime: String?,

    @SerializedName("end_time")
    val endTime: String?,
)