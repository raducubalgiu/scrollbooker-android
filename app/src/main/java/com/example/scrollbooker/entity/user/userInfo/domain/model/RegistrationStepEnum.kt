package com.example.scrollbooker.entity.user.userInfo.domain.model

enum class RegistrationStepEnum(val key: String) {
    // Shared
    COLLECT_USER_EMAIL_VALIDATION("collect_user_email_validation"),
    COLLECT_USER_USERNAME("collect_user_username"),
    COLLECT_USER_PHONE_HUMBER("collect_user_phone_number"),

    // Client
    COLLECT_CLIENT_BIRTHDATE("collect_client_birthdate"),
    COLLECT_CLIENT_GENDER("collect_client_gender"),
    COLLECT_CLIENT_LOCATION_PERMISSION("collect_client_location_permission"),

    // Business
    COLLECT_BUSINESS("collect_business"),
    COLLECT_BUSINESS_SERVICES("collect_business_services"),
    COLLECT_BUSINESS_SCHEDULES("collect_business_schedules"),
    COLLECT_BUSINESS_HAS_EMPLOYEES("collect_business_has_employees"),
    COLLECT_BUSINESS_VALIDATION("collect_business_validation");

    companion object {
        fun fromKeyOrNull(key: String?): RegistrationStepEnum? =
            key?.let { entries.find { it.key == key } }
    }
}