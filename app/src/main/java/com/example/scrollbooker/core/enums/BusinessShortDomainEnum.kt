package com.example.scrollbooker.core.enums

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.scrollbooker.ui.theme.Auto
import com.example.scrollbooker.ui.theme.Beauty
import com.example.scrollbooker.ui.theme.Medical

enum class BusinessShortDomainEnum(val key: String) {
    BEAUTY("Beauty"),
    AUTO("Auto"),
    MEDICAL("Medical"),
    UNKNOWN("Unknown");

    companion object {
        fun fromKey(key: String?): BusinessShortDomainEnum? =
            entries.firstOrNull() { it.key.equals(key, ignoreCase = true) }

        fun fromKeyOrUnknown(key: String?): BusinessShortDomainEnum =
            fromKey(key) ?: UNKNOWN
    }
}

@Composable
fun BusinessShortDomainEnum.toDomainColor(): Color {
    return when(this) {
        BusinessShortDomainEnum.BEAUTY -> Beauty
        BusinessShortDomainEnum.AUTO -> Auto
        BusinessShortDomainEnum.MEDICAL -> Medical
        BusinessShortDomainEnum.UNKNOWN -> Color.Gray
    }
}