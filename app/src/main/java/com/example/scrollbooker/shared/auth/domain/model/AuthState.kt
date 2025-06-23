package com.example.scrollbooker.shared.auth.domain.model
import com.example.scrollbooker.shared.user.userInfo.domain.model.RegistrationStepEnum

data class AuthState(
    val isValidated: Boolean,
    val registrationStep: RegistrationStepEnum?
)