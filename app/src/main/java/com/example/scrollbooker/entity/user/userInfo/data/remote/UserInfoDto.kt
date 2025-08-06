package com.example.scrollbooker.entity.user.userInfo.data.remote
import com.google.gson.annotations.SerializedName

data class UserInfoDto(
    val id: Int,

    val username: String,
    val fullname: String,

    @SerializedName("business_id")
    val businessId: Int,

    @SerializedName("business_type_id")
    val businessTypeId: Int,

    @SerializedName("is_validated")
    val isValidated: Boolean,

    @SerializedName("registration_step")
    val registrationStep: String?
)