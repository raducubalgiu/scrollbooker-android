package com.example.scrollbooker.entity.booking.products.domain.model

import com.example.scrollbooker.core.enums.FilterTypeEnum
import com.example.scrollbooker.core.enums.ProductTypeEnum
import com.example.scrollbooker.entity.nomenclature.filter.domain.model.SubFilter
import java.math.BigDecimal

data class Product(
    val id: Int,
    val name: String,
    val description: String?,
    val duration: Int,
    val price: BigDecimal,
    val priceWithDiscount: BigDecimal,
    val discount: BigDecimal,
    val userId: Int,
    val serviceId: Int,
    val businessId: Int,
    val currencyId: Int,
    val canBeBooked: Boolean,
    val type: ProductTypeEnum?,
    val sessionsCount: Int?,
    val validityDays: Int?,
    val filters: List<ProductFilter>
)

data class ProductFilter(
    val id: Int,
    val name: String,
    val subFilters: List<SubFilter>,
    val type: FilterTypeEnum?,
    val unit: String?,
    val minim: BigDecimal?,
    val maxim: BigDecimal?
)