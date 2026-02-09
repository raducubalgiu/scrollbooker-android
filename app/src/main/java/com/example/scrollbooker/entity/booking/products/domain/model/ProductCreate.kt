package com.example.scrollbooker.entity.booking.products.domain.model
import java.math.BigDecimal

data class ProductCreate(
    val name: String,
    val description: String,
    val price: BigDecimal,
    val priceWithDiscount: BigDecimal,
    val discount: BigDecimal,
    val duration: Int,
    val serviceId: Int,
    val businessId: Int,
    val currencyId: Int,
    val type: String,
    val sessionsCount: Int?,
    val validityDays: Int?,
    val canBeBooked: Boolean
)