package com.example.scrollbooker.ui.statistics

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.scrollbooker.R
import com.example.scrollbooker.components.customized.stats.StatBarRow
import com.example.scrollbooker.components.customized.stats.StatCard
import com.example.scrollbooker.entity.social.post.domain.model.PostAnalyticsSummary
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.bodySmall
import com.example.scrollbooker.ui.theme.labelSmall
import com.example.scrollbooker.ui.theme.titleMedium
import com.example.scrollbooker.ui.theme.titleSmall
import java.util.concurrent.TimeUnit

private data class QuickStat(
    val icon: Int,
    val labelResId: Int,
    val value: Int
)

@Composable
fun PostStatisticsContent(summary: PostAnalyticsSummary) {
    val groupedSources = remember(summary.sourceBreakdown) {
        summary.sourceBreakdown
            .groupBy { it.source?.labelResId ?: R.string.analytics_source_other }
            .map { (resId, items) ->
                val totalViews = items.sumOf { it.viewsCount }
                val minOrdinal = items.mapNotNull { it.source?.ordinal }.minOrNull() ?: Int.MAX_VALUE

                Triple(resId, totalViews, minOrdinal)
            }
            .sortedBy { it.third }
            .map { it.first to it.second }
    }

    val maxViews = remember(groupedSources) {
        groupedSources.maxOfOrNull { it.second } ?: 1
    }

    val quickStats = remember(summary) {
        listOf(
            QuickStat(R.drawable.ic_play_outline, R.string.totalViews, summary.viewsCount),
            QuickStat(R.drawable.ic_heart_solid, R.string.likes, summary.likeCount),
            QuickStat(R.drawable.ic_comment_outline, R.string.comments, summary.commentCount),
            QuickStat(R.drawable.ic_share, R.string.shares, summary.shareCount),
            QuickStat(R.drawable.ic_bookmark_outline, R.string.saves, summary.bookmarkCount)
        )
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        item {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                AsyncImage(
                    model = summary.thumbnailUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(130.dp)
                        .aspectRatio(9f / 12f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(SurfaceBG)
                )
            }
        }

        item {
            Row(modifier = Modifier.fillMaxWidth()) {
                quickStats.forEach { stat ->
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(stat.icon),
                            contentDescription = null,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        Text(
                            text = stat.value.toString(),
                            style = bodySmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = stringResource(stat.labelResId),
                            style = labelSmall,
                            color = Color.Gray
                        )
                    }
                }
            }
        }

        item {
            Text(
                text = stringResource(R.string.postStatisticsSummary),
                style = titleSmall,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatCard(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.views),
                    value = summary.viewsCount.toString(),
                    labelStyle = bodySmall,
                    valueStyle = titleMedium
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.uniqueViewers),
                    value = summary.uniqueViewersCount.toString(),
                    labelStyle = bodySmall,
                    valueStyle = titleMedium
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.watchTime),
                    value = formatMillis(summary.watchTimeMs),
                    labelStyle = bodySmall,
                    valueStyle = titleMedium
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.averageWatchTime),
                    value = formatMillis(summary.averageWatchTimeMs),
                    labelStyle = bodySmall,
                    valueStyle = titleMedium
                )
            }
        }

        if (groupedSources.isNotEmpty()) {
            item {
                Text(
                    text = stringResource(R.string.trafficSources),
                    style = titleSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                )
            }

            items(groupedSources) { (labelResId, viewsCount) ->
                val progress = viewsCount.toFloat() / maxViews.coerceAtLeast(1)

                StatBarRow(
                    label = stringResource(id = labelResId),
                    valueString = viewsCount.toString(),
                    progressPercentage = progress
                )
            }
        }
    }
}

private fun formatMillis(ms: Long): String {
    val hours = TimeUnit.MILLISECONDS.toHours(ms)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(ms) % 60
    val seconds = TimeUnit.MILLISECONDS.toSeconds(ms) % 60

    return when {
        hours > 0 -> "${hours}h ${minutes}m"
        minutes > 0 -> "${minutes}m ${seconds}s"
        else -> "${seconds}s"
    }
}
