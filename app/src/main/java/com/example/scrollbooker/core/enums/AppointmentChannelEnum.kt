package com.example.scrollbooker.core.enums

import androidx.annotation.StringRes
import com.example.scrollbooker.R

enum class AppointmentChannelEnum(
    val key: String,
    @StringRes val titleRes: Int
) {
    SCROLL_BOOKER("scroll_booker", R.string.scrollbooker),
    OWN_CLIENT("own_client", R.string.ownClient);

    companion object {
        fun fromKey(key: String): AppointmentChannelEnum? =
            AppointmentChannelEnum.entries.find { it.key == key }

        fun fromKeys(keys: List<String>): List<AppointmentChannelEnum> =
            keys.mapNotNull { AppointmentChannelEnum.Companion.fromKey(it) }
    }
}