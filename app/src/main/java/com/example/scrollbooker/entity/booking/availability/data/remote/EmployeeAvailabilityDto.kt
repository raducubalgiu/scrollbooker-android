package com.example.scrollbooker.entity.booking.availability.data.remote

import com.google.gson.annotations.SerializedName

data class EmployeeAvailabilityDto(
    @SerializedName("employee_id")
    val employeeId: Int,

    @SerializedName("has_availability")
    val hasAvailability: Boolean
)
