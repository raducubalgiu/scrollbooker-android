package com.example.scrollbooker.entity.search.data.remote

enum class SearchTypeEnum(val key: String) {
    USER("user"),
    SERVICE("service"),
    BUSINESS_TYPE("business_type"),
    KEYWORD("keyword");

    companion object {
        fun fromKey(key: String): SearchTypeEnum? =
            SearchTypeEnum.entries.find { it.key == key }

        fun fromKeys(keys: List<String>): List<SearchTypeEnum> =
            keys.mapNotNull { fromKey(it) }
    }
}