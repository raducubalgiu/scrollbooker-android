package com.example.scrollbooker.entity.booking.business.data.remote

import com.example.scrollbooker.entity.booking.appointment.data.remote.BusinessCoordinatesDto
import com.google.gson.annotations.SerializedName

data class UnapprovedBusinessDto(
    val id: Int,

    @SerializedName("fullname")
    val fullName: String,

    val username: String,
    val avatar: String?,
    val business: UnapprovedBusinessDataDto
)

data class UnapprovedBusinessDataDto(
    val id: Int,

    @SerializedName("has_employees")
    val hasEmployees: Boolean,

    val location: UnapprovedLocationDto,

    @SerializedName("business_type")
    val businessType: UnapprovedBusinessTypeDto
)

data class UnapprovedLocationDto(
    val coordinates: BusinessCoordinatesDto,
    val address: String
)

data class UnapprovedBusinessTypeDto(
    val id: Int,
    val name: String
)