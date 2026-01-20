package com.example.scrollbooker.entity.booking.products.data.remote

import com.example.scrollbooker.entity.nomenclature.filter.domain.model.SubFilter
import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class ProductDto(
    val id: Int,
    val name: String,
    val description: String,
    val duration: Int,
    val price: BigDecimal,

    @SerializedName("price_with_discount")
    val priceWithDiscount: BigDecimal,

    val discount: BigDecimal,

    @SerializedName("user_id")
    val userId: Int,

    @SerializedName("service_id")
    val serviceId: Int,

    @SerializedName("business_id")
    val businessId: Int,

    @SerializedName("currency_id")
    val currencyId: Int,

    @SerializedName("can_be_booked")
    val canBeBooked: Boolean,

    val filters: List<ProductFilterDto>
)

data class ProductFilterDto(
    val id: Int,
    val name: String,

    @SerializedName("sub_filters")
    val subFilters: List<SubFilter>,
    val type: String,
    val unit: String?,
    val minim: BigDecimal?,
    val maxim: BigDecimal?
)