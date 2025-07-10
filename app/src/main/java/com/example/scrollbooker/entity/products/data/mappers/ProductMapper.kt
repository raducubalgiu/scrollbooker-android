package com.example.scrollbooker.entity.products.data.mappers

import com.example.scrollbooker.entity.products.data.remote.ProductDto
import com.example.scrollbooker.entity.products.domain.model.Product

fun ProductDto.toDomain(): Product {
    return Product(
        id = id,
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