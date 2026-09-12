package com.example.scrollbooker.entity.booking.availability.data.remote
import com.google.gson.annotations.SerializedName

data class CalendarEventsBusinessResponseDto(
    @SerializedName("business_short_domain")
    val businessShortDomain: String,

    val employees: List<CalendarEventsBusinessEmployeeDto>
)

data class CalendarEventsBusinessEmployeeDto(
    val id: Int,
    val fullname: String,
    val username: String,
    val avatar: String?,
    val profession: String,
    val slots: List<CalendarEventsSlotDto>
)
