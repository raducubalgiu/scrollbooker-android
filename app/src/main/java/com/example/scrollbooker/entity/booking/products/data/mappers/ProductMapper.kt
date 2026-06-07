package com.example.scrollbooker.entity.booking.products.data.mappers

import com.example.scrollbooker.core.enums.FilterTypeEnum
import com.example.scrollbooker.core.enums.ProductTypeEnum
import com.example.scrollbooker.entity.booking.products.data.remote.ProductDto
import com.example.scrollbooker.entity.booking.products.data.remote.ProductVariantDto
import com.example.scrollbooker.entity.booking.products.data.remote.ProductFilterDto
import com.example.scrollbooker.entity.booking.products.data.remote.StartingOfferingDto
import com.example.scrollbooker.entity.booking.products.data.remote.ProductOfferingDto
import com.example.scrollbooker.entity.booking.products.data.remote.ProductOfferingUserDto
import com.example.scrollbooker.entity.booking.products.domain.model.StartingOffering
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.entity.booking.products.domain.model.ProductVariant
import com.example.scrollbooker.entity.booking.products.domain.model.ProductFilter
import com.example.scrollbooker.entity.booking.products.domain.model.ProductOffering
import com.example.scrollbooker.entity.booking.products.domain.model.ProductOfferingUser

fun ProductDto.toDomain(): Product {
    return Product(
        id = id,
        name = name,
        description = description,
        serviceId = serviceId,
        businessId = businessId,
        businessOwnerId = businessOwnerId,
        currencyId = currencyId,
        canBeBooked = canBeBooked,
        type = ProductTypeEnum.fromKey(type),
        sessionsCount = sessionsCount,
        validityDays = validityDays,
        hasDifferentPrices = hasDifferentPrices,
        startingOffering = startingOffering.toDomain(),
        variants = variants.map { it.toDomain() },
        filters = filters.map { it.toDomain() }
    )
}

fun StartingOfferingDto.toDomain(): StartingOffering {
    return StartingOffering(
        id = id,
        variantId = variantId,
        variantName = variantName,
        duration = duration,
        userId = userId,
        price = price,
        discount = discount,
        priceWithDiscount = priceWithDiscount
    )
}


fun ProductVariantDto.toDomain(): ProductVariant {
    return ProductVariant(
        id = id,
        name = name,
        duration = duration,
        startingOffering = startingOffering.toDomain(),
        hasDifferentPrices = hasDifferentPrices,
        offerings = offerings.map { it.toDomain() }
    )
}

fun ProductOfferingDto.toDomain(): ProductOffering {
    return ProductOffering(
        id = id,
        user = user.toDomain(),
        price = price,
        discount = discount,
        priceWithDiscount = priceWithDiscount
    )
}

fun ProductOfferingUserDto.toDomain(): ProductOfferingUser {
    return ProductOfferingUser(
        id = id,
        username = username,
        fullname = fullname,
        avatar = avatar,
        profession = profession
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
        maxim = maxim,
        displayAsTab = displayAsTab
    )
}