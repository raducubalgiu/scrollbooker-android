package com.example.scrollbooker.shared.user.userInfo.domain.model

enum class RegistrationStepEnum(val key: String) {
    COLLECT_BUSINESS_TYPE("collect_business_type"),
    COLLECT_BUSINESS_LOCATION("collect_business_location"),
    COLLECT_BUSINESS_SERVICES("collect_business_services"),
    COLLECT_BUSINESS_SCHEDULES("collect_business_schedules");

    companion object {
        fun fromKeyOrNull(key: String?): RegistrationStepEnum? {
            return entries.find { it.key == key }
        }
    }
}