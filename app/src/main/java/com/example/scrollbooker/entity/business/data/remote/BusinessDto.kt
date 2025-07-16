package com.example.scrollbooker.entity.business.data.remote
import com.example.scrollbooker.entity.appointment.data.remote.BusinessCoordinatesDto
import com.google.gson.annotations.SerializedName

data class BusinessDto(
    val id: Int,

    @SerializedName("business_type_id")
    val businessTypeId: Int,

    @SerializedName("owner_id")
    val ownerId: Int,

    val description: String,

    val timezone: String,

    val address: String,

    val coordinates: BusinessCoordinatesDto,

    @SerializedName("has_employees")
    val hasEmployees: Boolean
)