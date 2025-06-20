package com.example.scrollbooker.shared.products.data.remote

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

    val discount: BigDecimal
)