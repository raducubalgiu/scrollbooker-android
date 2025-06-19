package com.example.scrollbooker.screens.profile.settings.reportProblem.data.remote

import com.google.gson.annotations.SerializedName

data class ReportProblemRequest(
    val text: String,

    @SerializedName("user_id")
    val userId: Int
)
