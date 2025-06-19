package com.example.scrollbooker.shared.employmentRequest.domain.model

import com.example.scrollbooker.screens.profile.userSocial.domain.model.UserSocial
import com.example.scrollbooker.shared.profession.domain.model.Profession

data class EmploymentRequest(
    val id: Int,
    val status: String,
    val employee: UserSocial,
    val employer: UserSocial,
    val profession: Profession
)