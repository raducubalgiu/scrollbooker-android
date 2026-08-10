package com.example.scrollbooker.entity.booking.business.domain.model

import com.example.scrollbooker.entity.booking.appointment.data.remote.BusinessCoordinatesDto

data class UnapprovedBusiness(
    val id: Int,
    val fullName: String,
    val username: String,
    val avatar: String?,
    val business: UnapprovedBusinessData
)

data class UnapprovedBusinessData(
    val id: Int,
    val hasEmployees: Boolean,
    val location: UnapprovedLocation,
    val businessType: UnapprovedBusinessType
)

data class UnapprovedLocation(
    val coordinates: BusinessCoordinatesDto,
    val address: String
)

data class UnapprovedBusinessType(
    val id: Int,
    val name: String
)