package com.example.scrollbooker.core.enums

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R

enum class GenderTypeEnum(val key: String) {
    MALE("male"),
    FEMALE("female"),
    OTHER("other");

    companion object {
        fun fromKey(key: String?): GenderTypeEnum? =
            GenderTypeEnum.entries.find { it.key == key }

        fun fromKeys(keys: List<String>): List<GenderTypeEnum> =
            keys.mapNotNull { GenderTypeEnum.Companion.fromKey(it) }
    }

    @Composable
    fun getLabel(): String {
        return when(this) {
            MALE -> stringResource(R.string.male)
            FEMALE -> stringResource(R.string.female)
            OTHER -> stringResource(R.string.preferNotToSay)
        }
    }
}