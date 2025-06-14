package com.example.scrollbooker.feature.services.data.remote

import com.google.gson.annotations.SerializedName

data class AttachManyServicesRequest(
    @SerializedName("service_ids")
    val serviceIds: List<Int>
)