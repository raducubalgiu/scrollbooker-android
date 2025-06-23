package com.example.scrollbooker.shared.user.userInfo.domain.model

data class UserInfo(
    val id: Int,
    val businessId: Int,
    val businessTypeId: Int,
    val isValidated: Boolean,
    val registrationStep: RegistrationStepEnum?
)