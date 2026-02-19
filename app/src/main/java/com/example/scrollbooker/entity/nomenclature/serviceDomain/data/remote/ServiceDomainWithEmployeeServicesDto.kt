package com.example.scrollbooker.entity.nomenclature.serviceDomain.data.remote

import com.example.scrollbooker.entity.nomenclature.filter.data.remote.FilterDto
import com.google.gson.annotations.SerializedName

data class ServiceDomainWithEmployeeServicesDto(
    val id: Int,
    val name: String,
    val services: List<ServiceWithEmployeesDto>
)

data class ServiceWithEmployeesDto(
    val id: Int,
    val name: String,

    @SerializedName("short_name")
    val shortName: String,
    val filters: List<FilterDto>,
    val employees: List<ServiceEmployeeDto>,

    @SerializedName("products_count")
    val productsCount: Int
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