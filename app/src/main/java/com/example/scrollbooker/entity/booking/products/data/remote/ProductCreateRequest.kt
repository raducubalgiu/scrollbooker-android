package com.example.scrollbooker.entity.booking.products.data.remote

import com.example.scrollbooker.core.enums.ProductTypeEnum
import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class ProductWithFiltersCreateRequest(
    @SerializedName("product")
    val product: ProductCreateRequest,
    val filters: List<ProductFilterRequest>
)

data class ProductCreateRequest(
    val name: String,
    val description: String?,

    @SerializedName("service_domain_id")
    val serviceDomainId: Int,

    @SerializedName("service_id")
    val serviceId: Int,

    @SerializedName("business_id")
    val businessId: Int,

    @SerializedName("currency_id")
    val currencyId: Int,

    val type: String = ProductTypeEnum.SINGLE.key,

    @SerializedName("can_be_booked")
    val canBeBooked: Boolean = true,

    @SerializedName("sessions_count")
    val sessionsCount: Int? = null,

    @SerializedName("validity_days")
    val validityDays: Int? = null,

    val variants: List<ProductVariantRequest>
)

data class ProductVariantRequest(
    val name: String,
    val duration: Int,
    val offerings: List<ProductOfferingRequest>
)

data class ProductOfferingRequest(
    @SerializedName("user_id")
    val userId: Int,

    val price: BigDecimal,
    val discount: BigDecimal,

    @SerializedName("price_with_discount")
    val priceWithDiscount: BigDecimal
)

data class ProductFilterRequest(
    @SerializedName("filter_id")
    val filterId: Int,

    @SerializedName("sub_filter_ids")
    val subFilterIds: List<Int>,

    @SerializedName("is_not_applicable")
    val isNotApplicable: Boolean = false
)