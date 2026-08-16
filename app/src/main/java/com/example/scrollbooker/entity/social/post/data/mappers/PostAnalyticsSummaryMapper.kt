package com.example.scrollbooker.entity.social.post.data.mappers

import com.example.scrollbooker.core.enums.PostViewSourceEnum
import com.example.scrollbooker.entity.social.post.data.remote.PostAnalyticsSourceBreakdownItemDto
import com.example.scrollbooker.entity.social.post.data.remote.PostAnalyticsSummaryDto
import com.example.scrollbooker.entity.social.post.domain.model.PostAnalyticsSourceBreakdownItem
import com.example.scrollbooker.entity.social.post.domain.model.PostAnalyticsSummary

fun PostAnalyticsSummaryDto.toDomain(): PostAnalyticsSummary {
    return PostAnalyticsSummary(
        postId = this.postId,
        viewsCount = this.viewsCount,
        uniqueViewsCount = this.uniqueViewsCount,
        watchTimeMs = this.watchTimeMs,
        averageWatchTimeMs = this.averageWatchTimeMs,
        completionsCount = this.completionsCount,
        sourceBreakdown = this.sourceBreakdown.map { it.toDomain() }
    )
}

fun PostAnalyticsSourceBreakdownItemDto.toDomain(): PostAnalyticsSourceBreakdownItem {
    return PostAnalyticsSourceBreakdownItem(
        source = PostViewSourceEnum.fromKey(this.source),
        viewsCount = this.viewsCount
    )
}