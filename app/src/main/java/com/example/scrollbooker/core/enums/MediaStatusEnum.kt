package com.example.scrollbooker.core.enums

enum class MediaStatusEnum(val key: String) {
    PROCESSING("processing"),
    READY("ready"),
    FAILED("failed");

    companion object {
        fun fromKey(key: String): MediaStatusEnum? =
            MediaStatusEnum.entries.find { it.key == key }
    }
}
