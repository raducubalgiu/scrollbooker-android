package com.example.scrollbooker.core.nav.routes

import com.example.scrollbooker.shared.user.userInfo.domain.model.RegistrationStepEnum

sealed class AuthRoute(val route: String) {
    object Login: AuthRoute(route = "login")
    object Register: AuthRoute(route = "register")
    object Username: AuthRoute(route = "username")
    object BirthDate: AuthRoute(route = "birthdate")

    object CollectBusinessType: AuthRoute(route = RegistrationStepEnum.COLLECT_BUSINESS_TYPE.key)
    object CollectBusinessLocation: AuthRoute(route = RegistrationStepEnum.COLLECT_BUSINESS_LOCATION.key)
    object CollectBusinessServices: AuthRoute(route = RegistrationStepEnum.COLLECT_BUSINESS_SERVICES.key)
    object CollectBusinessSchedules: AuthRoute(route = RegistrationStepEnum.COLLECT_BUSINESS_SCHEDULES.key)
}