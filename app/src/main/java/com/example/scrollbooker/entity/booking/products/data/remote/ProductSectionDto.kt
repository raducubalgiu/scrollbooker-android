package com.example.scrollbooker.entity.booking.products.data.remote

import com.example.scrollbooker.entity.nomenclature.filter.domain.model.SubFilter
import com.google.gson.annotations.SerializedName

data class ProductSectionDto (
    @SerializedName("sub_filter")
    val subFilter: SubFilter?,

    val products: List<ProductDto>
)