package com.example.scrollbooker.navigation.routes

import com.example.scrollbooker.entity.user.userInfo.domain.model.RegistrationStepEnum

sealed class AuthRoute(val route: String) {
    object Login: AuthRoute(route = "login")

    object RegisterClient: AuthRoute(route = "registerClient")
    object RegisterBusiness: AuthRoute(route = "registerBusiness")

    object CollectEmailVerification: AuthRoute(route = RegistrationStepEnum.COLLECT_USER_EMAIL_VALIDATION.key)
    object CollectUserUsername: AuthRoute(route = RegistrationStepEnum.COLLECT_USER_USERNAME.key)

    object CollectClientBirthDate: AuthRoute(route = RegistrationStepEnum.COLLECT_CLIENT_BIRTHDATE.key)
    object CollectClientGender: AuthRoute(route = RegistrationStepEnum.COLLECT_CLIENT_GENDER.key)
    object CollectClientLocationPermission: AuthRoute(route = RegistrationStepEnum.COLLECT_CLIENT_LOCATION_PERMISSION.key)

    object CollectBusiness: AuthRoute(route = RegistrationStepEnum.COLLECT_BUSINESS.key)
    object CollectBusinessType: AuthRoute(route = "collect_business_type")
    object CollectBusinessDetails: AuthRoute(route = "collect_business_details")
    object CollectBusinessLocation: AuthRoute(route = "collect_business_location")
    object CollectBusinessGallery: AuthRoute(route = "collect_business_gallery")

    object CollectBusinessServices: AuthRoute(route = RegistrationStepEnum.COLLECT_BUSINESS_SERVICES.key)
    object CollectBusinessSchedules: AuthRoute(route = RegistrationStepEnum.COLLECT_BUSINESS_SCHEDULES.key)
    object CollectBusinessHasEmployees: AuthRoute(route = RegistrationStepEnum.COLLECT_BUSINESS_HAS_EMPLOYEES.key)
    object CollectBusinessValidation: AuthRoute(route = RegistrationStepEnum.COLLECT_BUSINESS_VALIDATION.key)
}