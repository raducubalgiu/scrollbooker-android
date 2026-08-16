package com.example.scrollbooker.core.enums

import com.google.gson.annotations.SerializedName

enum class PostViewPlatformEnum(val key: String) {
    @SerializedName("android") ANDROID("android"),
    @SerializedName("ios") IOS("ios"),
    @SerializedName("web") WEB("web");

    companion object {
        fun fromKey(key: String): PostViewPlatformEnum? =
            entries.find { it.key == key }

        fun fromKeys(keys: List<String>): List<PostViewPlatformEnum> =
            keys.mapNotNull { fromKey(it) }
    }
}