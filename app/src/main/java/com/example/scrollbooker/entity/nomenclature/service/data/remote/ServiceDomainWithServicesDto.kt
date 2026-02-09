package com.example.scrollbooker.entity.nomenclature.service.data.remote
import com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.model.ServiceDomain
import com.google.gson.annotations.SerializedName

data class ServiceDomainWithServicesDto(
    @SerializedName("service_domain")
    val serviceDomain: ServiceDomain,

    val services: List<ServicesWithEmployeesDto>
)

data class ServicesWithEmployeesDto(
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