package com.example.scrollbooker.entity.booking.business.domain.model
import com.example.scrollbooker.entity.booking.schedule.domain.model.Schedule
import kotlinx.serialization.SerialName

data class BusinessDetails(
    val id: Int,
    val owner: BusinessOwner,
    val location: BusinessLocation,

    @SerialName("has_employees")
    val hasEmployees: Boolean,

    @SerialName("media_files")
    val mediaFiles: List<BusinessMediaFile>,

    val schedules: List<Schedule>
)