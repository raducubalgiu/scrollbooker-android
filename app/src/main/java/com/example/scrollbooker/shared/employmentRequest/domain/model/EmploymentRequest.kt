package com.example.scrollbooker.shared.employmentRequest.domain.model
import com.example.scrollbooker.shared.profession.domain.model.Profession
import com.example.scrollbooker.shared.user.userSocial.domain.model.UserSocial

data class EmploymentRequest(
    val id: Int,
    val status: String,
    val employee: UserSocial,
    val employer: UserSocial,
    val profession: Profession
)