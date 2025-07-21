package com.example.scrollbooker.entity.nomenclature.consent.data.remote

import com.google.gson.annotations.SerializedName

data class ConsentDto(
    val id: Int,
    val name: String,
    val title: String,
    val text: String,
    val version: String,

    @SerializedName("created_at")
    val createdAt: String
)
