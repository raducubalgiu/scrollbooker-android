package com.example.scrollbooker.core.enums

enum class ProductTypeEnum(val key: String) {
    SINGLE("single"),
    PACK("pack"),
    MEMBERSHIP("membership");

    companion object {
        fun fromKey(key: String): ProductTypeEnum? =
            ProductTypeEnum.entries.find { it.key == key }

        fun fromKeys(keys: List<String>): List<ProductTypeEnum> =
            keys.mapNotNull { ProductTypeEnum.Companion.fromKey(it) }
    }
}