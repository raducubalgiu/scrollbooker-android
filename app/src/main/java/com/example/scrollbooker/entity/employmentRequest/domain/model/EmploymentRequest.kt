package com.example.scrollbooker.entity.employmentRequest.domain.model
import com.example.scrollbooker.entity.profession.domain.model.Profession
import com.example.scrollbooker.entity.user.userSocial.domain.model.UserSocial

data class EmploymentRequest(
    val id: Int,
    val status: String,
    val employee: UserSocial,
    val employer: UserSocial,
    val profession: Profession
)