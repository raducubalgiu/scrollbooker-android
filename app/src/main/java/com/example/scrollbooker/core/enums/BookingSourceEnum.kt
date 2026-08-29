package com.example.scrollbooker.core.enums

enum class BookingSourceEnum(val key: String) {
    BOOK_AGAIN("book_again"),

    EXPLORE_FEED("explore_feed"),
    FOLLOWING_FEED("following_feed"),

    PROFILE("profile"),
    PROFILE_GRID_POST_DETAIL("profile_grid_post_detail"),
    PROFILE_BOOKMARKS_POST_DETAIL("profile_bookmarks_post_detail"),
    VIDEO_REVIEWS("video_reviews"),

    SEARCH("search"),
    SEARCH_BUSINESS_PROFILE("search_business_profile"),

    OTHER("other");

    companion object {
        fun fromKey(key: String): BookingSourceEnum? =
            BookingSourceEnum.entries.find { it.key == key }

        fun fromKeys(keys: List<String>): List<BookingSourceEnum> =
            keys.mapNotNull { BookingSourceEnum.Companion.fromKey(it) }
    }
}