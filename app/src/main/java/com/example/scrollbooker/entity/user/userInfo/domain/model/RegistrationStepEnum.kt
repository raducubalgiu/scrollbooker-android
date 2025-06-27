package com.example.scrollbooker.entity.user.userInfo.domain.model

enum class RegistrationStepEnum(val key: String) {
    COLLECT_EMAIL_VERIFICATION("collect_email_verification"),

    COLLECT_USER_LOCATION_PERMISSION("collect_user_location_permission"),
    COLLECT_USER_USERNAME("collect_user_username"),
    COLLECT_USER_PHONE_HUMBER("collect_user_phone_number"),

    COLLECT_CLIENT_BIRTHDATE("collect_client_birthdate"),
    COLLECT_CLIENT_GENDER("collect_client_gender"),

    COLLECT_BUSINESS_TYPE("collect_business_type"),
    COLLECT_BUSINESS_LOCATION("collect_business_location"),
    COLLECT_BUSINESS_SERVICES("collect_business_services"),
    COLLECT_BUSINESS_SCHEDULES("collect_business_schedules"),
    COLLECT_BUSINESS_VALIDATION("collect_business_validation");

    companion object {
        fun fromKeyOrNull(key: String?): RegistrationStepEnum? {
            return entries.find { it.key == key }
        }
    }
}