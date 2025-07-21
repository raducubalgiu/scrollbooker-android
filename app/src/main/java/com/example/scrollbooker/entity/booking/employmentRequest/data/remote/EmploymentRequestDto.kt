package com.example.scrollbooker.entity.booking.employmentRequest.data.remote
import com.example.scrollbooker.entity.nomenclature.profession.data.remote.ProfessionDto
import com.example.scrollbooker.entity.user.userSocial.data.remote.UserSocialDto
import com.google.gson.annotations.SerializedName

data class EmploymentRequestDto(
    val id: Int,

    @SerializedName("created_at")
    val createdAt: String,

    val status: String,
    val employee: UserSocialDto,
    val employer: UserSocialDto,
    val profession: ProfessionDto
)