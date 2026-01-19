package com.example.scrollbooker.core.enums

enum class FilterTypeEnum(val key: String) {
    OPTIONS("options"),
    RANGE("range");

    companion object {
        fun fromKey(key: String?): FilterTypeEnum? =
            FilterTypeEnum.entries.find { it.key == key }

        fun fromKeys(keys: List<String>): List<FilterTypeEnum> =
            keys.mapNotNull { FilterTypeEnum.Companion.fromKey(it) }
    }
}