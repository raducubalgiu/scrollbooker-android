package com.example.scrollbooker.core.enums

enum class EmploymentRequestStatusEnum(val key: String) {
    PENDING("pending"),
    ACCEPTED("accepted"),
    DENIED("denied");

    companion object {
        fun fromKey(key: String): EmploymentRequestStatusEnum? =
            EmploymentRequestStatusEnum.entries.find { it.key == key }

        fun fromKeys(keys: List<String>): List<EmploymentRequestStatusEnum> =
            keys.mapNotNull { fromKey(it) }
    }
}