package com.example.scrollbooker.shared.employmentRequest.data.remote

import com.example.scrollbooker.screens.profile.userSocial.data.remote.UserSocialDto
import com.example.scrollbooker.shared.profession.data.remote.ProfessionDto

data class EmploymentRequestDto(
    val id: Int,
    val status: String,
    val employee: UserSocialDto,
    val employer: UserSocialDto,
    val profession: ProfessionDto
)