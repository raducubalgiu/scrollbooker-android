package com.example.scrollbooker.core.enums

enum class ConsentEnum(val key: String) {
    EMPLOYMENT_REQUESTS_INITIATION("EMPLOYMENT_REQUESTS_INITIATION"),
    EMPLOYMENT_REQUESTS_ACCEPTANCE("EMPLOYMENT_REQUESTS_ACCEPTANCE"),
    EMPLOYMENT_REQUESTS_RESIGNATION_BY_BUSINESS("EMPLOYMENT_REQUESTS_RESIGNATION_BY_BUSINESS"),
    EMPLOYMENT_REQUESTS_RESIGNATION_BY_EMPLOYEE("EMPLOYMENT_REQUESTS_RESIGNATION_BY_EMPLOYEE");

    companion object {
        fun fromKey(key: String): ConsentEnum? =
            ConsentEnum.entries.find { it.key == key }

        fun fromKeys(keys: List<String>): List<ConsentEnum> =
            keys.mapNotNull { ConsentEnum.Companion.fromKey(it) }
    }
}