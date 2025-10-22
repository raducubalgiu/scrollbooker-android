package com.example.scrollbooker.entity.nomenclature.service.domain.model
import com.google.gson.annotations.SerializedName

data class ServiceWithEmployees(
    val service: Service,

    @SerializedName("products_count")
    val productsCount: Int,

    val employees: List<ServiceEmployee>
)

data class ServiceEmployee(
    val id: Int,
    val fullName: String,
    val username: String,
    val avatar: String?,
    val profession: String,
    val ratingsAverage: Float
)