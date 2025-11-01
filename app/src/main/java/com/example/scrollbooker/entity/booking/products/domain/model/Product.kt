package com.example.scrollbooker.entity.booking.products.domain.model

import java.math.BigDecimal

data class Product(
    val id: Int,
    val name: String,
    val description: String,
    val duration: Int,
    val price: BigDecimal,
    val priceWithDiscount: BigDecimal,
    val discount: BigDecimal,
    val userId: Int,
    val serviceId: Int,
    val businessId: Int,
    val currencyId: Int,
    val subFilters: List<ProductSubFilter>
)

data class ProductSubFilter(
    val id: Int,
    val name: String,
    val filter: ProductFilter
)

data class ProductFilter(
    val id: Int,
    val name: String
)