package com.example.scrollbooker.feature.myBusiness.employmentRequests.domain.model
import com.example.scrollbooker.feature.userSocial.domain.model.UserSocial

data class EmploymentRequest(
    val id: Int,
    val status: String,
    val employee: UserSocial,
    val employer: UserSocial,
    val profession: Profession
)

data class Profession(
    val id: Int,
    val name: String
)