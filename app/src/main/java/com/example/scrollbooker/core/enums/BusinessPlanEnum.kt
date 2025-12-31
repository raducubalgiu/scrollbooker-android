package com.example.scrollbooker.core.enums

enum class BusinessPlanEnum(val key: String) {
    FREE("Free"),
    STANDARD("Standard"),
    PREMIUM("Premium");

    companion object {
        fun fromKey(key: String): BusinessPlanEnum? =
            BusinessPlanEnum.entries.find { it.key == key }

        fun fromKeys(keys: List<String>): List<BusinessPlanEnum> =
            keys.mapNotNull { BusinessPlanEnum.Companion.fromKey(it) }
    }
}