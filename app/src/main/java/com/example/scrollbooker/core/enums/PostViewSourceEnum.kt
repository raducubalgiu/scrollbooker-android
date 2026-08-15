package com.example.scrollbooker.core.enums

import com.google.gson.annotations.SerializedName

enum class PostViewSourceEnum(val key: String) {
    @SerializedName("search_video_feed") SEARCH_VIDEO_FEED("search_video_feed"),
    @SerializedName("explore_feed") EXPLORE_FEED("explore_feed"),
    @SerializedName("following_feed") FOLLOWING_FEED("following_feed"),
    @SerializedName("post_detail") POST_DETAIL("post_detail"),
    @SerializedName("bookmark_post_detail") BOOKMARK_POST_DETAIL("bookmark_post_detail"),
    @SerializedName("video_reviews") VIDEO_REVIEWS("video_reviews"),
    @SerializedName("other") OTHER("other");

    companion object {
        fun fromKey(key: String): PostViewSourceEnum? =
            entries.find { it.key == key }

        fun fromKeys(keys: List<String>): List<PostViewSourceEnum> =
            keys.mapNotNull { fromKey(it) }
    }
}