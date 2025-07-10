package com.example.scrollbooker.entity.products.data.remote
import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class ProductCreateDto(
    val name: String,
    val description: String,
    val price: BigDecimal,

    @SerializedName("price_with_discount")
    val priceWithDiscount: BigDecimal,

    val discount: BigDecimal,
    val duration: Int,

    @SerializedName("service_id")
    val serviceId: Int,

    @SerializedName("business_id")
    val businessId: Int,

    @SerializedName("currency_id")
    val currencyId: Int,
)