package com.example.scrollbooker.entity.booking.products.domain.model

import com.example.scrollbooker.core.enums.FilterTypeEnum
import com.example.scrollbooker.core.enums.ProductTypeEnum
import com.example.scrollbooker.entity.nomenclature.filter.domain.model.SubFilter
import com.example.scrollbooker.ui.booking.SelectedBookingItem
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

fun Product.getDurationText(minutes: Int): String {
    if (minutes == 0) return "0min"

    val hours = minutes / 60
    val remainingMinutes = minutes % 60

    val hoursPart = if (hours > 0) "${hours}h" else ""
    val minutesPart = if (remainingMinutes > 0) "${remainingMinutes}min" else ""

    return listOf(hoursPart, minutesPart)
        .filter { it.isNotEmpty() }
        .joinToString(" ")
}

fun Product.getFiltersSummary(): String {
    val filterParts = this.filters.mapNotNull { filter ->
        when (filter.type) {
            FilterTypeEnum.OPTIONS -> {
                filter.subFilters.joinToString(" & ") { it.name }
            }
            FilterTypeEnum.RANGE -> {
                val minim = filter.minim
                val maxim = filter.maxim
                val unit = filter.unit ?: ""

                when {
                    minim != null && maxim == null -> "> $minim $unit".trim()
                    minim == null && maxim != null -> "< $maxim $unit".trim()
                    minim != null && maxim != null -> "$minim - $maxim $unit".trim()
                    else -> null
                }
            }
            else -> null
        }
    }

    return filterParts.joinToString(" • ")
}

fun ProductVariant.toBookingItem(product: Product): SelectedBookingItem {
    return SelectedBookingItem(
        productId = product.id,
        variantId = this.id,
        variantDuration = this.duration,
        offerings = this.offerings,
        productName = product.name,
        variantName = this.name
    )
}
