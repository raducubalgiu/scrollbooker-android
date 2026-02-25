package com.example.scrollbooker.entity.booking.products.data.remote

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class ProductCreateRequestDto(
    @SerializedName("product")
    val product: ProductCreateDto,

    @SerializedName("service_domain_id")
    val serviceDomainId: Int,

    val filters: List<AddProductFilterRequest>
)

data class AddProductFilterRequest(
    @SerializedName("filter_id")
    val filterId: Int,

    @SerializedName("sub_filter_ids")
    val subFilterIds: List<Int> = emptyList(),

    @SerializedName("is_not_applicable")
    val isNotApplicable: Boolean,

    val type: String,
    val minim: BigDecimal?,
    val maxim: BigDecimal?
)