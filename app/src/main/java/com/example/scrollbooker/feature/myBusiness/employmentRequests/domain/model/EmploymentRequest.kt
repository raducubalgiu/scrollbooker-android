package com.example.scrollbooker.feature.myBusiness.employmentRequests.domain.model

import com.example.scrollbooker.shared.professions.domain.model.Profession
import com.example.scrollbooker.feature.userSocial.domain.model.UserSocial

data class EmploymentRequest(
    val id: Int,
    val status: String,
    val employee: UserSocial,
    val employer: UserSocial,
    val profession: Profession
)