package com.example.scrollbooker.entity.booking.employmentRequest.data.remote

import com.google.gson.annotations.SerializedName

data class EmploymentRequestCreateDto(
    @SerializedName("employee_id")
    val employeeId: Int,

    @SerializedName("profession_id")
    val professionId: Int,

    @SerializedName("consent_id")
    val consentId: Int
)