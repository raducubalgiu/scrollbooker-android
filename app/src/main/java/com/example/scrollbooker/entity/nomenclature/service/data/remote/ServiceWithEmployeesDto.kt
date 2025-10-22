package com.example.scrollbooker.entity.nomenclature.service.data.remote
import com.google.gson.annotations.SerializedName

data class ServiceWithEmployeesDto(
    val service: ServiceDto,

    @SerializedName("products_count")
    val productsCount: Int,

    val employees: List<ServiceEmployeeDto>
)

data class ServiceEmployeeDto(
    val id: Int,

    @SerializedName("fullname")
    val fullName: String,

    val username: String,
    val avatar: String?,
    val profession: String,

    @SerializedName("ratings_average")
    val ratingsAverage: Float
)