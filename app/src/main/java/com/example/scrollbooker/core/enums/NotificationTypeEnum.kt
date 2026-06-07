package com.example.scrollbooker.core.enums

enum class NotificationTypeEnum(val key: String) {
    FOLLOW("follow"),
    LIKE_POST("like_post"),
    COMMENT_POST("comment_post"),
    REPOST("repost"),
    MENTION_POST("mention_post"),
    APPOINTMENT_BOOKED("appointment_booked"),
    APPOINTMENT_CANCELED("appointment_canceled"),
    APPOINTMENT_RESCHEDULED("appointment_rescheduled"),
    APPOINTMENT_REMINDER("appointment_reminder"),
    APPOINTMENT_REVIEWED("appointment_reviewed"),
    EMPLOYMENT_REQUEST("employment_request"),
    EMPLOYMENT_REQUEST_ACCEPT("employment_request_accept"),
    EMPLOYMENT_REQUEST_DENIED("employment_request_denied"),
    BUSINESS_VALIDATION("business_validation"),
    UNKNOWN("UNKNOWN");

    companion object {
        fun fromKey(key: String?): NotificationTypeEnum =
            NotificationTypeEnum.entries.find { it.key == key } ?: UNKNOWN

        fun fromKeys(keys: List<String>): List<NotificationTypeEnum> =
            keys.map { NotificationTypeEnum.fromKey(it) }
    }
}
