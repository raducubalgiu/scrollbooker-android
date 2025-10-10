package com.example.scrollbooker.core.enums

enum class NotificationTypeEnum(val key: String) {
    FOLLOW("follow"),
    EMPLOYMENT_REQUEST("employment_request"),
    EMPLOYMENT_REQUEST_ACCEPT("employment_request_accept"),
    EMPLOYMENT_REQUEST_DENIED("employment_request_denied"),
    UNKNOWN("UNKNOWN");

    companion object {
        fun fromKey(key: String?): NotificationTypeEnum =
            NotificationTypeEnum.entries.find { it.key == key } ?: UNKNOWN

        fun fromKeys(keys: List<String>): List<NotificationTypeEnum> =
            keys.map { NotificationTypeEnum.Companion.fromKey(it) }
    }
}