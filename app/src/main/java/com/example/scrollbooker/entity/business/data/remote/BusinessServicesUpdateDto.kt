package com.example.scrollbooker.entity.business.data.remote

import com.google.gson.annotations.SerializedName

data class BusinessServicesUpdateRequest(
    @SerializedName("service_ids")
    val serviceIds: List<Int>
)