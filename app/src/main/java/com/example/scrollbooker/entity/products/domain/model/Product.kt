package com.example.scrollbooker.entity.products.domain.model

import java.math.BigDecimal

data class Product(
    val id: Int,
    val name: String,
    val description: String,
    val duration: Int,
    val price: BigDecimal,
    val priceWithDiscount: BigDecimal,
    val discount: BigDecimal
)