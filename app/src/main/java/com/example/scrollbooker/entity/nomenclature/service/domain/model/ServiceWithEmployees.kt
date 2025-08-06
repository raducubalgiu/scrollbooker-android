package com.example.scrollbooker.entity.nomenclature.service.domain.model
import com.example.scrollbooker.entity.user.userSocial.domain.model.UserSocial
import com.google.gson.annotations.SerializedName

data class ServiceWithEmployees(
    val service: Service,

    @SerializedName("products_count")
    val productsCount: Int,

    val employees: List<UserSocial>
)