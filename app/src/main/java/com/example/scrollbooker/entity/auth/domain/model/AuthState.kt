package com.example.scrollbooker.entity.auth.domain.model
import com.example.scrollbooker.entity.user.userInfo.domain.model.RegistrationStepEnum

data class AuthState(
    val isValidated: Boolean,
    val registrationStep: RegistrationStepEnum?
)