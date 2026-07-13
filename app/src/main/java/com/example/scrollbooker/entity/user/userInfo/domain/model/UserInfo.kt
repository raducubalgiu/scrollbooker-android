package com.example.scrollbooker.entity.user.userInfo.domain.model

import com.example.scrollbooker.core.enums.RegistrationStepEnum

data class UserInfo(
    val id: Int,
    val username: String,
    val fullname: String,
    val profession: String,
    val avatar: String?,
    val businessId: Int?,
    val businessOwnerId: Int?,
    val businessTypeId: Int?,
    val hasEmployees: Boolean,
    val isValidated: Boolean,
    val registrationStep: RegistrationStepEnum?
)