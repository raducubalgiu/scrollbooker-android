package com.example.scrollbooker.entity.booking.products.data.mappers

import com.example.scrollbooker.entity.booking.products.data.remote.ProductDto
import com.example.scrollbooker.entity.booking.products.data.remote.ProductFilterDto
import com.example.scrollbooker.entity.booking.products.data.remote.ProductSubFilterDto
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.entity.booking.products.domain.model.ProductFilter
import com.example.scrollbooker.entity.booking.products.domain.model.ProductSubFilter

fun ProductDto.toDomain(): Product {
    return Product(
        id = id,
        name = name,
        description = description,
        duration = duration,
        price = price,
        priceWithDiscount = priceWithDiscount,
        discount = discount,
        userId = userId,
        serviceId = serviceId,
        businessId = businessId,
        currencyId = currencyId,
        subFilters = subFilters.map { it.toDomain() }
    )
}

fun ProductSubFilterDto.toDomain(): ProductSubFilter {
    return ProductSubFilter(
        id = id,
        name = name,
        filter = filter.toDomain()
    )
}

fun ProductFilterDto.toDomain(): ProductFilter {
    return ProductFilter(
        id = id,
        name = name
    )
}