package com.example.scrollbooker.feature.myBusiness.employeeDismissal.data.remote

import com.google.gson.annotations.SerializedName

data class EmployeeDismissalDto(
    val id: Int,
    val title: String,
    val text: String,
    val version: String,

    @SerializedName("created_at")
    val createdAt: String
)