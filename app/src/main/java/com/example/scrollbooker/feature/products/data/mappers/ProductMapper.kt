package com.example.scrollbooker.feature.products.data.mappers

import com.example.scrollbooker.feature.products.data.remote.ProductDto
import com.example.scrollbooker.feature.products.domain.model.Product

fun ProductDto.toDomain(): Product {
    return Product(
        id = id,
        name = name,
        description = description,
        duration = duration,
        price = price,
        priceWithDiscount = priceWithDiscount,
        discount = discount
    )
}