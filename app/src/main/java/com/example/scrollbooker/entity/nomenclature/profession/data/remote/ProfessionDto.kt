package com.example.scrollbooker.entity.nomenclature.profession.data.remote

import com.google.gson.annotations.SerializedName

data class ProfessionDto(
    val id: Int,
    val name: String,

    @SerializedName("business_domain_id")
    val businessDomainId: Int
)