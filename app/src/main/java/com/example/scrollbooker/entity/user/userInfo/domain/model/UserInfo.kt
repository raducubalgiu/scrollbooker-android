package com.example.scrollbooker.entity.user.userInfo.domain.model

data class UserInfo(
    val id: Int,
    val username: String,
    val fullname: String,
    val avatar: String?,
    val businessId: Int,
    val businessTypeId: Int,
    val hasEmployees: Boolean,
    val isValidated: Boolean,
    val registrationStep: RegistrationStepEnum?
)