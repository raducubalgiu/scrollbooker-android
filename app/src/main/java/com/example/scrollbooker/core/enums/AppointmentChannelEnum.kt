package com.example.scrollbooker.core.enums

enum class AppointmentChannelEnum(val key: String) {
    SCROLL_BOOKER("scroll_booker"),
    OWN_CLIENT("own_client");

    companion object {
        fun fromKey(key: String): AppointmentChannelEnum? =
            AppointmentChannelEnum.entries.find { it.key == key }

        fun fromKeys(keys: List<String>): List<AppointmentChannelEnum> =
            keys.mapNotNull { AppointmentChannelEnum.Companion.fromKey(it) }
    }
}