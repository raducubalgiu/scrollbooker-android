package com.example.scrollbooker.entity.booking.products.data.mappers

import com.example.scrollbooker.entity.booking.products.data.remote.LinkedProductsBusinessDto
import com.example.scrollbooker.entity.booking.products.data.remote.LinkedProductsResponseDto
import com.example.scrollbooker.entity.booking.products.domain.model.LinkedProducts
import com.example.scrollbooker.entity.booking.products.domain.model.LinkedProductsBusiness

fun LinkedProductsBusinessDto.toDomain(): LinkedProductsBusiness {
    return LinkedProductsBusiness(
        id = id,
        fullname = fullname,
        username = username,
        profession = profession,
        avatar = avatar,
        ratingsAverage = ratingsAverage,
        ratingsCount = ratingsCount,
        distanceKm = distanceKm,
        address = address
    )
}

fun LinkedProductsResponseDto.toDomain(): LinkedProducts {
    return LinkedProducts(
        business = business.toDomain(),
        products = products.map { it.toDomain() }
    )
}
