package com.example.scrollbooker.shared.products.data.mappers

import com.example.scrollbooker.shared.products.data.remote.ProductDto
import com.example.scrollbooker.shared.products.domain.model.Product

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