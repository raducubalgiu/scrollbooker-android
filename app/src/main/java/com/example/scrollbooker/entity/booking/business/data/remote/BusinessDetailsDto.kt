package com.example.scrollbooker.entity.booking.business.data.remote

import com.example.scrollbooker.entity.booking.schedule.data.remote.ScheduleDto
import com.google.gson.annotations.SerializedName

data class BusinessDetailsDto(
    val id: Int,
    val owner: BusinessOwnerDto,
    val location: BusinessLocationDto,

    @SerializedName("has_employees")
    val hasEmployees: Boolean,

    @SerializedName("media_files")
    val mediaFiles: List<BusinessMediaFileDto>?,

    val schedules: List<ScheduleDto>
)