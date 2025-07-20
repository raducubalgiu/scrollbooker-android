package com.example.scrollbooker.entity.search.data.remote

import com.example.scrollbooker.entity.user.userSocial.domain.model.UserSocial
import com.google.gson.annotations.SerializedName

data class SearchDto (
    val type: String,

    val label: String,
    val user: UserSocial?,
    val service: SearchServiceBusinessTypeDto?,

    @SerializedName("business_type")
    val businessType: SearchServiceBusinessTypeDto?,
)

data class SearchServiceBusinessTypeDto(
    val id: Int,
    val name: String
)