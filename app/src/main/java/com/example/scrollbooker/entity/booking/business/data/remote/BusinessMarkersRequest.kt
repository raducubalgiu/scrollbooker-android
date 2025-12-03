package com.example.scrollbooker.entity.booking.business.data.remote

import com.example.scrollbooker.entity.booking.appointment.domain.model.BusinessCoordinates
import com.google.gson.annotations.SerializedName

data class BusinessMarkersRequest(
    val bbox: BusinessBoundingBox,

    val zoom: Float,

    @SerializedName("max_markers")
    val maxMarkers: Int? = null,

    @SerializedName("business_domain_id")
    val businessDomainId: Int? = null,

    @SerializedName("business_type_id")
    val businessTypeId: Int? = null,

    @SerializedName("service_id")
    val serviceId: Int? = null,

    @SerializedName("subfilter_ids")
    val subFilterIds: List<Int>? = null,

    @SerializedName("user_location")
    val userLocation: BusinessCoordinates? = null,

    @SerializedName("max_price")
    val maxPrice: Float? = null,

    val sort: String?
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