package com.example.scrollbooker.feature.myBusiness.employmentRequests.data.remote

import com.example.scrollbooker.shared.professions.data.remote.ProfessionDto
import com.example.scrollbooker.feature.userSocial.data.remote.UserSocialDto

data class EmploymentRequestDto(
    val id: Int,
    val status: String,
    val employee: UserSocialDto,
    val employer: UserSocialDto,
    val profession: ProfessionDto
)