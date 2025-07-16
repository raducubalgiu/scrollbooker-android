package com.example.scrollbooker.entity.employmentRequest.data.remote
import com.example.scrollbooker.entity.profession.data.remote.ProfessionDto
import com.example.scrollbooker.entity.user.userSocial.data.remote.UserSocialDto
import com.google.gson.annotations.SerializedName
import org.threeten.bp.ZonedDateTime

data class EmploymentRequestDto(
    val id: Int,

    @SerializedName("created_at")
    val createdAt: String,

    val status: String,
    val employee: UserSocialDto,
    val employer: UserSocialDto,
    val profession: ProfessionDto
)