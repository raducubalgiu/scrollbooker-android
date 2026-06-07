package com.example.scrollbooker.entity.booking.products.data.remote

import com.example.scrollbooker.entity.nomenclature.filter.domain.model.SubFilter
import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class ProductDto(
    val id: Int,
    val name: String,
    val description: String?,

    @SerializedName("service_id")
    val serviceId: Int,

    @SerializedName("business_id")
    val businessId: Int,

    @SerializedName("business_owner_id")
    val businessOwnerId: Int,

    @SerializedName("currency_id")
    val currencyId: Int,

    @SerializedName("can_be_booked")
    val canBeBooked: Boolean,

    val type: String,

    @SerializedName("sessions_count")
    val sessionsCount: Int?,

    @SerializedName("validity_days")
    val validityDays: Int?,

    @SerializedName("has_different_prices")
    val hasDifferentPrices: Boolean,

    @SerializedName("starting_offering")
    val startingOffering: StartingOfferingDto,

    val variants: List<ProductVariantDto>,

    val filters: List<ProductFilterDto>,
)

data class ProductVariantDto(
    val id: Int,
    val name: String,
    val duration: Int,

    @SerializedName("starting_offering")
    val startingOffering: StartingOfferingDto,

    @SerializedName("has_different_prices")
    val hasDifferentPrices: Boolean,

    val offerings: List<ProductOfferingDto>
)

data class StartingOfferingDto(
    val id: Int,

    @SerializedName("variant_id")
    val variantId: Int,

    @SerializedName("variant_name")
    val variantName: String?,

    val duration: Int,

    @SerializedName("user_id")
    val userId: Int,

    val price: BigDecimal,
    val discount: BigDecimal,

    @SerializedName("price_with_discount")
    val priceWithDiscount: BigDecimal
)

data class ProductOfferingDto(
    val id: Int,

    val user: ProductOfferingUserDto,
    val price: BigDecimal,
    val discount: BigDecimal,

    @SerializedName("price_with_discount")
    val priceWithDiscount: BigDecimal
)

data class ProductOfferingUserDto(
    val id: Int,
    val username: String,
    val fullname: String,
    val profession: String,
    val avatar: String?
)

data class ProductFilterDto(
    val id: Int,
    val name: String,

    @SerializedName("sub_filters")
    val subFilters: List<SubFilter>,
    val type: String,
    val unit: String?,
    val minim: BigDecimal?,
    val maxim: BigDecimal?,

    @SerializedName("display_as_tab")
    val displayAsTab: Boolean
)