package com.example.scrollbooker.entity.booking.products.data.mappers

import com.example.scrollbooker.entity.booking.products.data.remote.BusinessServicesWithProductsDto
import com.example.scrollbooker.entity.booking.products.domain.model.BusinessServicesWithProducts
import com.example.scrollbooker.entity.nomenclature.service.data.mappers.toDomain

fun BusinessServicesWithProductsDto.toDomain() = BusinessServicesWithProducts(
    service = service.toDomain(),
    products = products.map { it.toDomain() }
)