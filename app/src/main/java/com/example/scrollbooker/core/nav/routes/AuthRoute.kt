package com.example.scrollbooker.core.nav.routes

import com.example.scrollbooker.entity.user.userInfo.domain.model.RegistrationStepEnum

sealed class AuthRoute(val route: String) {
    object Login: AuthRoute(route = "login")

    object RegisterClient: AuthRoute(route = "registerClient")
    object RegisterBusiness: AuthRoute(route = "registerBusiness")

    object CollectEmailVerification: AuthRoute(route = RegistrationStepEnum.COLLECT_EMAIL_VERIFICATION.key)
    object CollectUserUsername: AuthRoute(route = RegistrationStepEnum.COLLECT_USER_USERNAME.key)

    object CollectClientBirthDate: AuthRoute(route = RegistrationStepEnum.COLLECT_CLIENT_BIRTHDATE.key)
    object CollectClientGender: AuthRoute(route = RegistrationStepEnum.COLLECT_CLIENT_GENDER.key)

    object CollectBusinessType: AuthRoute(route = RegistrationStepEnum.COLLECT_BUSINESS_TYPE.key)
    object CollectBusinessLocation: AuthRoute(route = RegistrationStepEnum.COLLECT_BUSINESS_LOCATION.key)
    object CollectBusinessServices: AuthRoute(route = RegistrationStepEnum.COLLECT_BUSINESS_SERVICES.key)
    object CollectBusinessSchedules: AuthRoute(route = RegistrationStepEnum.COLLECT_BUSINESS_SCHEDULES.key)
}