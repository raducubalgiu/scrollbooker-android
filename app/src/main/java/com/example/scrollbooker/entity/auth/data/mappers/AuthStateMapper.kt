package com.example.scrollbooker.entity.auth.data.mappers
import com.example.scrollbooker.entity.auth.data.remote.AuthStateDto
import com.example.scrollbooker.entity.auth.domain.model.AuthState
import com.example.scrollbooker.entity.user.userInfo.domain.model.RegistrationStepEnum

fun AuthStateDto.toDomain(): AuthState {
    return AuthState(
        isValidated = isValidated,
        registrationStep = RegistrationStepEnum.fromKeyOrNull(registrationStep)
    )
}