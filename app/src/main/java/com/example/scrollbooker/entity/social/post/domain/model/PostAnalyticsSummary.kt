package com.example.scrollbooker.entity.social.post.domain.model

import com.example.scrollbooker.core.enums.PostViewSourceEnum

data class PostAnalyticsSummary(
    val postId: Int,
    val thumbnailUrl: String?,
    val viewsCount: Int,
    val uniqueViewersCount: Int,
    val watchTimeMs: Long,
    val averageWatchTimeMs: Long,
    val completionsCount: Int,
    val likeCount: Int,
    val commentCount: Int,
    val shareCount: Int,
    val bookmarkCount: Int,
    val sourceBreakdown: List<PostAnalyticsSourceBreakdownItem>
)

data class PostAnalyticsSourceBreakdownItem(
    val source: PostViewSourceEnum?,
    val viewsCount: Int
)