package com.example.scrollbooker.entity.booking.products.domain.model

import com.example.scrollbooker.entity.nomenclature.filter.domain.model.SubFilter
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
    val subFilters: List<SubFilter>
)