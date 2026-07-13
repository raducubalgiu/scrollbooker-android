package com.example.scrollbooker.entity.auth.domain.model
import com.example.scrollbooker.core.enums.RegistrationStepEnum

data class AuthState(
    val isValidated: Boolean,
    val registrationStep: RegistrationStepEnum?
)