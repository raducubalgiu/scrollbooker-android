package com.example.scrollbooker.ui.profile

enum class PostTabEnum(val key: String) {
    POSTS("posts"),
    BOOKMARKS("bookmarks");

    companion object {
        fun fromKey(key: String): PostTabEnum? =
            PostTabEnum.entries.find { it.key == key }

        fun fromKeys(keys: List<String>): List<PostTabEnum> =
            keys.mapNotNull { PostTabEnum.Companion.fromKey(it) }
    }
}