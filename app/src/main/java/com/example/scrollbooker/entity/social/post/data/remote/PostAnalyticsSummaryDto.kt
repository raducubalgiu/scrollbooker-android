package com.example.scrollbooker.entity.social.post.data.remote
import com.google.gson.annotations.SerializedName

data class PostAnalyticsSummaryDto(
    @SerializedName("post_id")
    val postId: Int,

    @SerializedName("thumbnail_url")
    val thumbnailUrl: String?,

    @SerializedName("views_count")
    val viewsCount: Int,

    @SerializedName("unique_viewers_count")
    val uniqueViewersCount: Int,

    @SerializedName("watch_time_ms")
    val watchTimeMs: Long,

    @SerializedName("average_watch_time_ms")
    val averageWatchTimeMs: Long,

    @SerializedName("completions_count")
    val completionsCount: Int,

    @SerializedName("like_count")
    val likeCount: Int,

    @SerializedName("comment_count")
    val commentCount: Int,

    @SerializedName("share_count")
    val shareCount: Int,

    @SerializedName("bookmark_count")
    val bookmarkCount: Int,

    @SerializedName("source_breakdown")
    val sourceBreakdown: List<PostAnalyticsSourceBreakdownItemDto>
)

data class PostAnalyticsSourceBreakdownItemDto(
    @SerializedName("source")
    val source: String,

    @SerializedName("views_count")
    val viewsCount: Int
)