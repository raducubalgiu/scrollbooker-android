package com.example.scrollbooker.entity.booking.business.data.remote

import com.example.scrollbooker.entity.booking.appointment.domain.model.BusinessCoordinates
import com.google.gson.annotations.SerializedName

data class BusinessMarkerDto(
    val owner: BusinessOwnerDto,

    @SerializedName("business_short_domain")
    val businessShortDomain: String,

    val address: String,
    val coordinates: BusinessCoordinates,

    @SerializedName("is_primary")
    val isPrimary: Boolean,

    @SerializedName("has_video")
    val hasVideo: Boolean,

    @SerializedName("media_preview")
    val mediaPreview: BusinessMediaPreviewDto?,
)

data class BusinessMediaPreviewDto(
    val type: String,

    @SerializedName("thumbnail_url")
    val thumbnailUrl: String?,

    @SerializedName("preview_video_url")
    val previewVideoUrl: String?
)