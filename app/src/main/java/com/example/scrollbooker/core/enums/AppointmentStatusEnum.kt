package com.example.scrollbooker.core.enums

enum class AppointmentStatusEnum(val key: String) {
    IN_PROGRESS("in_progress"),
    FINISHED("finished"),
    CANCELED("canceled");

    companion object {
        fun fromKey(key: String): AppointmentStatusEnum? =
            AppointmentStatusEnum.entries.find { it.key == key }

        fun fromKeys(keys: List<String>): List<AppointmentStatusEnum> =
            keys.mapNotNull { AppointmentStatusEnum.Companion.fromKey(it) }
    }
}