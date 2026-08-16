package com.example.scrollbooker.entity.social.post.domain.model

import com.example.scrollbooker.core.enums.PostViewSourceEnum

data class PostAnalyticsSummary(
    val postId: Int,
    val viewsCount: Int,
    val uniqueViewsCount: Int,
    val watchTimeMs: Long,
    val averageWatchTimeMs: Long,
    val completionsCount: Int,
    val sourceBreakdown: List<PostAnalyticsSourceBreakdownItem>
)

data class PostAnalyticsSourceBreakdownItem(
    val source: PostViewSourceEnum?,
    val viewsCount: Int
)