package com.example.scrollbooker.ui.search.components

import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.util.UnstableApi
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.ui.search.SearchPlayerViewModel
import com.example.scrollbooker.ui.theme.titleMedium
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

data class SearchBusinessCardModel(
    val id: Int,
    val title: String,
    val url: String,
    val coverUrl: String,
    val longitude: Float,
    val latitude: Float
)

@OptIn(UnstableApi::class)
@Composable
fun SearchResultsList(
    dummyBusinesses: List<SearchBusinessCardModel>
) {
    val videoViewModel: SearchPlayerViewModel = hiltViewModel()
    val listState = rememberLazyListState()

    LaunchedEffect(listState) {
        val idleFlow = snapshotFlow { !listState.isScrollInProgress }
        val visibleIdFlow = snapshotFlow { listState.mostVisibleKey<Int>() }

        combine(idleFlow, visibleIdFlow) { idle, id -> idle to id }
            .filter { (idle, id) -> idle && id != null }
            .map { (_, id) -> id!! }
            .distinctUntilChanged()
            .collect { id ->
                val item = dummyBusinesses.find { it.id == id }

                if (item != null) {
                    videoViewModel.play(item.id, item.url)
                } else {
                    videoViewModel.pause()
                }
            }
    }

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Box(modifier = Modifier
                .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    modifier = Modifier
                        .padding(bottom = SpacingM),
                    text = "200 de rezultate",
                    style = titleMedium,
                    color = Color.Gray
                )
            }
        }

        items(
            items = dummyBusinesses,
            key = { it.id }
        ) { v ->
            Box(Modifier.fillMaxWidth()) {
                SearchBusinessCard(
                    viewModel = videoViewModel,
                    //imageUrl = "https://picsum.photos/600/300",
                    id = v.id,
                    url = v.url,
                    coverUrl = v.coverUrl,
                    name = "Ida Spa Dorobanti",
                    rating = 5.0,
                    reviews = 4327,
                    location = "Sector 1, București",
                    services = listOf(
                        ServiceUiModel("NEW Intensive Muscle Release Massage", "1 hr - 1 hr 30 mins", 280),
                        ServiceUiModel("King Balinese Massage", "1 hr - 1 hr 30 mins", 280),
                        ServiceUiModel("Neuro Sedative Relaxing Massage", "1 hr - 1 hr 30 mins", 290),
                    )
                )
            }
        }
    }
}

inline fun <reified  K : Any> LazyListState.mostVisibleKey(): K? {
    val info = this.layoutInfo
    val items = info.visibleItemsInfo
    if(items.isEmpty()) return null

    val viewportStart = info.viewportStartOffset
    val viewportEnd = info.viewportEndOffset

    var bestKey: K? = null
    var bestVisiblePx = 0

    for (item in items) {
        val key = item.key as? K ?: continue
        val itemStart = item.offset
        val itemEnd = item.offset + item.size
        val visibleStart = maxOf(itemStart, viewportStart)
        val visibleEnd = minOf(itemEnd, viewportEnd)
        val visiblePx = (visibleEnd - visibleStart).coerceAtLeast(0)

        if(visiblePx > bestVisiblePx) {
            bestVisiblePx = visiblePx
            bestKey = key
        }
    }
    return bestKey
}