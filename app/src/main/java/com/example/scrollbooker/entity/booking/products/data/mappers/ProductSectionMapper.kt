package com.example.scrollbooker.entity.booking.products.data.mappers

import com.example.scrollbooker.entity.booking.products.data.remote.ProductSectionDto
import com.example.scrollbooker.entity.booking.products.domain.model.ProductSection

fun ProductSectionDto.toDomain(): ProductSection {
    return ProductSection(
        subFilter = subFilter,
        products = products.map { it.toDomain() }
    )
}