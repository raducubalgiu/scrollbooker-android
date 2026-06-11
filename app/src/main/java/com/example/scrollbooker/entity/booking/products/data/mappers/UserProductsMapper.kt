package com.example.scrollbooker.entity.booking.products.data.mappers

import com.example.scrollbooker.entity.booking.products.data.remote.BusinessServicesWithProductsDto
import com.example.scrollbooker.entity.booking.products.data.remote.UserProductsDto
import com.example.scrollbooker.entity.booking.products.domain.model.BusinessServicesWithProducts
import com.example.scrollbooker.entity.booking.products.domain.model.UserProducts
import com.example.scrollbooker.entity.nomenclature.service.data.mappers.toDomain

fun UserProductsDto.toDomain(): UserProducts {
    return UserProducts(
        totalCount = totalCount,
        data = data.map { it.toDomain() }
    )
}

fun BusinessServicesWithProductsDto.toDomain(): BusinessServicesWithProducts {
    return BusinessServicesWithProducts(
        service = service.toDomain(),
        products = products.map { it.toDomain() }
    )
}