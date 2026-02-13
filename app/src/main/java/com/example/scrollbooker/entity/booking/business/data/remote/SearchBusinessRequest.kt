package com.example.scrollbooker.entity.booking.business.data.remote

import com.example.scrollbooker.entity.booking.appointment.domain.model.BusinessCoordinates
import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class SearchBusinessRequest(
    val bbox: BusinessBoundingBox,

    val zoom: Float,

    @SerializedName("max_markers")
    val maxMarkers: Int? = null,

    @SerializedName("business_domain_id")
    val businessDomainId: Int? = null,

    @SerializedName("service_domain_id")
    val serviceDomainId: Int? = null,

    @SerializedName("service_id")
    val serviceId: Int? = null,

    @SerializedName("subfilter_ids")
    val subFilterIds: List<Int>? = null,

    @SerializedName("user_location")
    val userLocation: BusinessCoordinates? = null,

    @SerializedName("max_price")
    val maxPrice: BigDecimal? = null,

    val sort: String?,

    @SerializedName("has_discount")
    val hasDiscount: Boolean = false,

    @SerializedName("start_date")
    val startDate: String? = null,

    @SerializedName("end_date")
    val endDate: String? = null,

    @SerializedName("start_time")
    val startTime: String? = null,

    @SerializedName("end_time")
    val endTime: String? = null
)

data class BusinessBoundingBox(
    @SerializedName("min_lng")
    val minLng: Float,

    @SerializedName("max_lng")
    val maxLng: Float,

    @SerializedName("min_lat")
    val minLat: Float,

    @SerializedName("max_lat")
    val maxLat: Float
)