package com.example.scrollbooker.components.customized.post.sheets.statistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.customized.stats.StatBarRow
import com.example.scrollbooker.components.customized.stats.StatCard
import com.example.scrollbooker.entity.social.post.domain.model.PostAnalyticsSummary
import java.util.concurrent.TimeUnit
import kotlin.collections.component1
import kotlin.collections.component2


@Composable
fun StatisticsContent(summary: PostAnalyticsSummary) {
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

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatCard(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.views),
                    value = summary.viewsCount.toString()
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.uniqueViewers),
                    value = summary.uniqueViewersCount.toString()
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
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.averageWatchTime),
                    value = formatMillis(summary.averageWatchTimeMs)
                )
            }
        }

        if (groupedSources.isNotEmpty()) {
            item {
                Text(
                    text = stringResource(R.string.trafficSources),
                    style = MaterialTheme.typography.titleMedium,
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