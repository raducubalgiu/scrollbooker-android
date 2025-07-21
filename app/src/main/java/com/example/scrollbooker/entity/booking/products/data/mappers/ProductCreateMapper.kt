package com.example.scrollbooker.entity.booking.products.data.mappers

import com.example.scrollbooker.entity.booking.products.data.remote.ProductCreateDto
import com.example.scrollbooker.entity.booking.products.domain.model.ProductCreate

fun ProductCreate.toDto(): ProductCreateDto {
    return ProductCreateDto(
        name = name,
        description = description,
        duration = duration,
        price = price,
        priceWithDiscount = priceWithDiscount,
        discount = discount,
        serviceId = serviceId,
        businessId = businessId,
        currencyId = currencyId
    )
}