package com.example.scrollbooker.entity.booking.business.data.remote

import com.example.scrollbooker.entity.booking.appointment.domain.model.BusinessCoordinates
import com.example.scrollbooker.entity.booking.products.data.remote.ProductDto
import com.google.gson.annotations.SerializedName

data class BusinessSheetDto(
    val owner: BusinessOwnerDto,

    @SerializedName("business_short_domain")
    val businessShortDomain: String,

    val address: String,
    val coordinates: BusinessCoordinates,

    @SerializedName("is_primary")
    val isPrimary: Boolean,

    @SerializedName("has_video")
    val hasVideo: Boolean,

    @SerializedName("media_files")
    val mediaFiles: List<BusinessMediaFileDto>,

    val products: List<ProductDto>,
)