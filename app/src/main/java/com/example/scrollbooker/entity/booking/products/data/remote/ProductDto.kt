package com.example.scrollbooker.entity.booking.products.data.remote

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

    @SerializedName("sub_filters")
    val subFilters: List<ProductSubFilterDto>
)

data class ProductSubFilterDto(
    val id: Int,
    val name: String,
    val filter: ProductFilterDto
)

data class ProductFilterDto(
    val id: Int,
    val name: String
)