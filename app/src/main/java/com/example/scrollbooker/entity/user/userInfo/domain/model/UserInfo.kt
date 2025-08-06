package com.example.scrollbooker.entity.user.userInfo.domain.model

data class UserInfo(
    val id: Int,
    val username: String,
    val fullname: String,
    val businessId: Int,
    val businessTypeId: Int,
    val isValidated: Boolean,
    val registrationStep: RegistrationStepEnum?
)