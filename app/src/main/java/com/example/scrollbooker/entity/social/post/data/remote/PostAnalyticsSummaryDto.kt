package com.example.scrollbooker.entity.social.post.data.remote
import com.google.gson.annotations.SerializedName

data class PostAnalyticsSummaryDto(
    @SerializedName("post_id")
    val postId: Int,

    @SerializedName("views_count")
    val viewsCount: Int,

    @SerializedName("unique_views_count")
    val uniqueViewsCount: Int,

    @SerializedName("watch_time_ms")
    val watchTimeMs: Long,

    @SerializedName("average_watch_time_ms")
    val averageWatchTimeMs: Long,

    @SerializedName("completions_count")
    val completionsCount: Int,

    @SerializedName("source_breakdown")
    val sourceBreakdown: List<PostAnalyticsSourceBreakdownItemDto>
)

data class PostAnalyticsSourceBreakdownItemDto(
    @SerializedName("source")
    val source: String,

    @SerializedName("views_count")
    val viewsCount: Int
)