package com.example.scrollbooker.entity.booking.products.domain.model

import com.example.scrollbooker.core.enums.FilterTypeEnum
import com.example.scrollbooker.core.enums.ProductTypeEnum
import com.example.scrollbooker.entity.nomenclature.filter.domain.model.SubFilter
import java.math.BigDecimal

data class Product(
    val id: Int,
    val name: String,
    val description: String?,
    val serviceId: Int,
    val businessId: Int,
    val businessOwnerId: Int,
    val currencyId: Int,
    val canBeBooked: Boolean,

    val type: ProductTypeEnum?,
    val sessionsCount: Int?,
    val validityDays: Int?,
    val hasDifferentPrices: Boolean,

    val startingOffering: StartingOffering,
    val variants: List<ProductVariant>,

    val filters: List<ProductFilter>,
)

data class ProductVariant(
    val id: Int,
    val name: String,
    val duration: Int,
    val startingOffering: StartingOffering,
    val hasDifferentPrices: Boolean,

    val offerings: List<ProductOffering>
)

data class StartingOffering(
    val id: Int,
    val variantId: Int,
    val variantName: String?,

    val duration: Int,
    val userId: Int,

    val price: BigDecimal,
    val discount: BigDecimal,
    val priceWithDiscount: BigDecimal
)

data class ProductOffering(
    val id: Int,

    val user: ProductOfferingUser,
    val price: BigDecimal,
    val discount: BigDecimal,
    val priceWithDiscount: BigDecimal
)

data class ProductOfferingUser(
    val id: Int,
    val username: String,
    val fullname: String,
    val profession: String,
    val avatar: String?
)

data class ProductFilter(
    val id: Int,
    val name: String,
    val subFilters: List<SubFilter>,
    val type: FilterTypeEnum?,
    val unit: String?,
    val minim: BigDecimal?,
    val maxim: BigDecimal?,
    val displayAsTab: Boolean
)