package com.example.scrollbooker.entity.booking.products.data.remote

import com.example.scrollbooker.entity.nomenclature.service.data.remote.ServiceDto
import com.google.gson.annotations.SerializedName

data class UserProductsDto(
    @SerializedName("total_count")
    val totalCount: Int,

    val data: List<BusinessServicesWithProductsDto>
)

data class BusinessServicesWithProductsDto(
    val service: ServiceDto,
    val products: List<ProductDto>
)