package com.example.scrollbooker.feature.myBusiness.data.mappers

import com.example.scrollbooker.feature.myBusiness.data.remote.products.ProductDto
import com.example.scrollbooker.feature.myBusiness.domain.model.Product

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