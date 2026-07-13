package com.example.scrollbooker.entity.user.userInfo.data.mappers

import com.example.scrollbooker.entity.user.userInfo.data.remote.UserInfoDto
import com.example.scrollbooker.core.enums.RegistrationStepEnum
import com.example.scrollbooker.entity.user.userInfo.domain.model.UserInfo

fun UserInfoDto.toDomain(): UserInfo {
    return UserInfo(
        id = id,
        username = username,
        fullname = fullname,
        profession = profession,
        avatar = avatar,
        businessId = businessId,
        businessOwnerId = businessOwnerId,
        businessTypeId = businessTypeId,
        hasEmployees = hasEmployees,
        isValidated = isValidated,
        registrationStep = RegistrationStepEnum.fromKeyOrNull(registrationStep)
    )
}