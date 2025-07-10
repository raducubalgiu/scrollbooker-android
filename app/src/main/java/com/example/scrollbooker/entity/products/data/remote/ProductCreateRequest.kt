package com.example.scrollbooker.entity.products.data.remote

import com.google.gson.annotations.SerializedName

data class ProductCreateRequestDto(
    @SerializedName("product")
    val product: ProductCreateDto,

    @SerializedName("sub_filters")
    val subFilters: List<Int>
)