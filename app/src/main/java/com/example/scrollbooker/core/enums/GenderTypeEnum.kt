package com.example.scrollbooker.core.enums

enum class GenderTypeEnum(val key: String) {
    MALE("male"),
    FEMALE("female"),
    OTHER("other");

    companion object {
        fun fromKey(key: String): GenderTypeEnum? =
            GenderTypeEnum.entries.find { it.key == key }
    }
}