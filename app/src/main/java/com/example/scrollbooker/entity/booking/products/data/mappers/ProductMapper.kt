package com.example.scrollbooker.entity.booking.products.data.mappers

import com.example.scrollbooker.core.enums.FilterTypeEnum
import com.example.scrollbooker.entity.booking.products.data.remote.ProductDto
import com.example.scrollbooker.entity.booking.products.data.remote.ProductFilterDto
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.entity.booking.products.domain.model.ProductFilter

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
        canBeBooked = canBeBooked,
        filters = filters.map { it.toDomain() }
    )
}

fun ProductFilterDto.toDomain(): ProductFilter {
    return ProductFilter(
        id = id,
        name = name,
        subFilters = subFilters,
        type = FilterTypeEnum.fromKey(type),
        unit = unit,
        minim = minim,
        maxim = maxim
    )
}