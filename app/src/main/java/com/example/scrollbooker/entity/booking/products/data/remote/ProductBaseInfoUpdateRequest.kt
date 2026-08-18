package com.example.scrollbooker.entity.booking.products.data.remote

import com.example.scrollbooker.core.enums.ProductTypeEnum
import com.google.gson.annotations.SerializedName

data class ProductBaseInfoUpdateRequest(
    val name: String,
    val description: String?,

    @SerializedName("service_domain_id")
    val serviceDomainId: Int,

    @SerializedName("service_id")
    val serviceId: Int,

    @SerializedName("can_be_booked")
    val canBeBooked: Boolean = true,

    @SerializedName("type")
    val type: String = ProductTypeEnum.SINGLE.key,

    @SerializedName("sessions_count")
    val sessionsCount: Int? = null,

    @SerializedName("validity_days")
    val validityDays: Int? = null,

    val filters: List<ProductFilterRequest>
)