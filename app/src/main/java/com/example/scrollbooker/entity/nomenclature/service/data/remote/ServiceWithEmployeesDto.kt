package com.example.scrollbooker.entity.nomenclature.service.data.remote

import com.example.scrollbooker.entity.user.userSocial.data.remote.UserSocialDto
import com.google.gson.annotations.SerializedName

data class ServiceWithEmployeesDto(
    val service: ServiceDto,

    @SerializedName("products_count")
    val productsCount: Int,

    val employees: List<UserSocialDto>
)