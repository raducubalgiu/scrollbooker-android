package com.example.scrollbooker.core.enums

import androidx.annotation.StringRes
import com.google.gson.annotations.SerializedName
import com.example.scrollbooker.R

enum class PostViewSourceEnum(val key: String, @StringRes val labelResId: Int) {
    @SerializedName("explore_feed")
    EXPLORE_FEED("explore_feed", R.string.analytics_source_explore),

    @SerializedName("following_feed")
    FOLLOWING_FEED("following_feed", R.string.analytics_source_following),

    @SerializedName("search_video_feed")
    SEARCH_VIDEO_FEED("search_video_feed", R.string.analytics_source_search),

    @SerializedName("post_detail")
    POST_DETAIL("post_detail", R.string.analytics_source_profile),

    @SerializedName("bookmark_post_detail")
    BOOKMARK_POST_DETAIL("bookmark_post_detail", R.string.analytics_source_profile),

    @SerializedName("video_reviews")
    VIDEO_REVIEWS("video_reviews", R.string.analytics_source_reviews),

    @SerializedName("other")
    OTHER("other", R.string.analytics_source_other);

    companion object {
        fun fromKey(key: String): PostViewSourceEnum? =
            entries.find { it.key == key }
    }
}
