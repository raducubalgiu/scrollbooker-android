package com.example.scrollbooker.shared.employmentRequest.data.remote
import com.example.scrollbooker.shared.profession.data.remote.ProfessionDto
import com.example.scrollbooker.shared.user.userSocial.data.remote.UserSocialDto

data class EmploymentRequestDto(
    val id: Int,
    val status: String,
    val employee: UserSocialDto,
    val employer: UserSocialDto,
    val profession: ProfessionDto
)