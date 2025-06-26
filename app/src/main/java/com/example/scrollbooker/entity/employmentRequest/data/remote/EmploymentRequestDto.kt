package com.example.scrollbooker.entity.employmentRequest.data.remote
import com.example.scrollbooker.entity.profession.data.remote.ProfessionDto
import com.example.scrollbooker.entity.user.userSocial.data.remote.UserSocialDto

data class EmploymentRequestDto(
    val id: Int,
    val status: String,
    val employee: UserSocialDto,
    val employer: UserSocialDto,
    val profession: ProfessionDto
)