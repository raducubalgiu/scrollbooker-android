package com.example.scrollbooker.components.customized.post.sheets.statistics

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import com.example.scrollbooker.components.core.sheet.SheetHeader
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.entity.social.post.domain.model.PostAnalyticsSummary
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.bodyMedium
import com.example.scrollbooker.ui.theme.titleLarge
import java.util.concurrent.TimeUnit

@Composable
fun PostStatisticsSheet(
    postId: Int,
    onClose: () -> Unit
) {
    val viewModel: PostStatisticsViewModel = hiltViewModel()

    LaunchedEffect(postId) {
        viewModel.setPostId(postId)
    }

    val uiState by viewModel.analyticsState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(fraction = 0.85f)
    ) {
        SheetHeader(
            title = "Statistici Postare",
            onClose = onClose
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .padding(
                    start = BasePadding,
                    end = BasePadding,
                    bottom = BasePadding
                )
        ) {
            when (val state = uiState) {
                is FeatureState.Loading -> LoadingScreen()
                is FeatureState.Success -> StatisticsContent(summary = state.data)
                is FeatureState.Error -> ErrorScreen()
            }
        }
    }
}

@Composable
private fun StatisticsContent(summary: PostAnalyticsSummary) {
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
                    label = "Vizualizări",
                    value = summary.viewsCount.toString()
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    label = "Vizitatori Unici",
                    value = summary.uniqueViewsCount.toString()
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
                    label = "Timp total",
                    value = formatMillis(summary.watchTimeMs
                    )
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    label = "Medie vizualizare",
                    value = formatMillis(summary.averageWatchTimeMs
                    )
                )
            }
        }

        if (groupedSources.isNotEmpty()) {
            item {
                Text(
                    text = "Surse de trafic",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                )
            }

            items(groupedSources) { (labelResId, viewsCount) ->
                SourceBreakdownRow(
                    sourceName = stringResource(id = labelResId),
                    viewsCount = viewsCount,
                    maxViews = maxViews
                )
            }
        }
    }
}

@Composable
private fun StatCard(
    modifier: Modifier,
    label: String,
    value: String
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(SurfaceBG),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = label, style = bodyMedium, color = OnSurfaceBG)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value, style = titleLarge, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun SourceBreakdownRow(
    sourceName: String,
    viewsCount: Int,
    maxViews: Int
) {
    val progress = viewsCount.toFloat() / maxViews.coerceAtLeast(1)

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = sourceName, style =bodyMedium, fontWeight = FontWeight.Medium)
            Text(text = viewsCount.toString(), style = bodyMedium, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(4.dp))

        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = Primary,
            trackColor = SurfaceBG
        )
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
