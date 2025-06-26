package com.example.scrollbooker.entity.service.data.remote

import com.google.gson.annotations.SerializedName

data class AttachManyServicesRequest(
    @SerializedName("service_ids")
    val serviceIds: List<Int>
)