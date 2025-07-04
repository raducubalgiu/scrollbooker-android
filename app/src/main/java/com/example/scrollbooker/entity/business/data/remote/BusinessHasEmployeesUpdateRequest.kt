package com.example.scrollbooker.entity.business.data.remote

import com.google.gson.annotations.SerializedName

data class BusinessHasEmployeesUpdateRequest(
    @SerializedName("has_employees")
    val hasEmployees: Boolean
)