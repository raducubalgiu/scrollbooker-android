package com.example.scrollbooker.entity.booking.products.data.remote

import com.example.scrollbooker.entity.nomenclature.service.data.remote.ServiceDto

data class BusinessServicesWithProductsDto(
    val service: ServiceDto,
    val products: List<ProductDto>
)