package com.example.scrollbooker.ui.search
import BottomBar
import android.annotation.SuppressLint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.util.UnstableApi
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.sheet.Sheet
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.navigation.bottomBar.MainTab
import com.example.scrollbooker.navigation.routes.MainRoute
import com.example.scrollbooker.ui.search.components.SearchBusinessCard
import com.example.scrollbooker.ui.search.components.SearchHeader
import com.example.scrollbooker.ui.search.components.SearchSheetHeader
import com.example.scrollbooker.ui.search.components.ServiceUiModel
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.titleMedium
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.math.abs
import kotlin.math.roundToInt

private const val HALF_FRACTION = 0.7f
enum class SheetStage { Collapsed, HalfExpanded, Expanded }

fun SheetStage.targetOffsetPx(totalHeightPx: Float, collapsedHeightPx: Float): Float {
    return when(this) {
        SheetStage.Collapsed -> totalHeightPx - collapsedHeightPx
        SheetStage.HalfExpanded -> totalHeightPx * (1f - HALF_FRACTION)
        SheetStage.Expanded -> 0f
    }
}

data class SearchBusinessCardModel(
    val id: Int,
    val title: String,
    val url: String,
    val coverUrl: String
)

val dummyBusinesses = listOf(
    SearchBusinessCardModel(
        id=1,
        title="Video 1",
        url = "https://media.scrollbooker.ro/business-video-1.mp4",
        coverUrl = "https://media.scrollbooker.ro/business-video-1-cover.jpeg"
    ),
    SearchBusinessCardModel(
        id=2, title="Video 2",
        url = "https://media.scrollbooker.ro/business-video-2.mp4",
        coverUrl = "https://media.scrollbooker.ro/business-video-2-cover.jpeg"
    ),
    SearchBusinessCardModel(
        id=3, title="Video 3",
        url = "https://media.scrollbooker.ro/business-video-3.mp4",
        coverUrl = "https://media.scrollbooker.ro/business-video-3-cover.jpeg"
    ),
    SearchBusinessCardModel(
        id=4, title="Video 4",
        url = "https://media.scrollbooker.ro/business-video-4.mp4",
        coverUrl = "https://media.scrollbooker.ro/business-video-4-cover.jpeg"
    ),
    SearchBusinessCardModel(
        id=5,
        title="Video 5",
        url = "https://media.scrollbooker.ro/business-video-5.mp4",
        coverUrl = "https://media.scrollbooker.ro/business-video-5-cover.jpeg"
    ),
    SearchBusinessCardModel(
        id=6,
        title="Video 6",
        url = "https://media.scrollbooker.ro/business-video-6.mp4",
        coverUrl = "https://media.scrollbooker.ro/business-video-6-cover.jpeg"
    ),
    SearchBusinessCardModel(
        id=7,
        title="Video 7",
        url = "https://media.scrollbooker.ro/business-video-7.mp4",
        coverUrl = "https://media.scrollbooker.ro/business-video-7-cover.jpeg"
    ),
    SearchBusinessCardModel(
        id=8, title="Video 8",
        url = "https://media.scrollbooker.ro/business-video-8.mp4",
        coverUrl = "https://media.scrollbooker.ro/business-video-8-cover.jpeg"
    ),
)

@androidx.annotation.OptIn(UnstableApi::class)
@SuppressLint("UnusedBoxWithConstraintsScope")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onNavigateToBusinessProfile: () -> Unit,
    appointmentsNumber: Int,
    onChangeTab: (MainTab) -> Unit
) {
    val videoViewModel: SearchPlayerViewModel = hiltViewModel()

    val scope = rememberCoroutineScope()
    var stage by rememberSaveable { mutableStateOf(SheetStage.HalfExpanded) }
    val isMapReady by viewModel.isMapReady.collectAsState()

    val sheetState = rememberModalBottomSheetState()

    if(sheetState.isVisible) {
        Sheet(
            sheetState = sheetState,
            onClose = { scope.launch { sheetState.hide() } }
        ) {
            Text("Hello World")
            Text("Hello World")
            Text("Hello World")
            Text("Hello World")
            Text("Hello World")
            Text("Hello World")
            Text("Hello World")
            Text("Hello World")
            Text("Hello World")
        }
        }

    Scaffold(
        bottomBar = {
            BottomBar(
                appointmentsNumber = appointmentsNumber,
                currentTab = MainTab.Search,
                currentRoute = MainRoute.Search.route,
                onChangeTab = onChangeTab
            )
        }
    ) { innerPadding ->
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val density = LocalDensity.current

            val bottomBarPadDp = innerPadding.calculateBottomPadding()
            var headerHeightDp by remember { mutableStateOf(0.dp) }

            val availableHeightDp = (maxHeight - headerHeightDp - bottomBarPadDp).coerceAtLeast(0.dp)
            val availableHeightPx = with(density) { availableHeightDp.toPx() }

            var collapsedHeightDp by remember { mutableStateOf(10.dp) }
            val collapsedHeightPx = with(density) { collapsedHeightDp.toPx() }

            val offset = remember {
                Animatable(stage.targetOffsetPx(availableHeightPx, collapsedHeightPx))
            }

            LaunchedEffect(availableHeightPx, stage) {
                offset.snapTo(stage.targetOffsetPx(availableHeightPx, collapsedHeightPx))
            }

            suspend fun animateTo(target: SheetStage) {
                val targetPx = target.targetOffsetPx(availableHeightPx, collapsedHeightPx)
                offset.stop()
                offset.animateTo(
                    targetPx,
                    animationSpec = tween(200, easing = FastOutSlowInEasing)
                )
                stage = target
            }

            val minPx = SheetStage.Expanded.targetOffsetPx(availableHeightPx, collapsedHeightPx)
            val maxPx = SheetStage.Collapsed.targetOffsetPx(availableHeightPx, collapsedHeightPx)

            val dragState = rememberDraggableState { delta ->
                val newOffset = (offset.value + delta).coerceIn(minPx, maxPx)
                scope.launch { offset.snapTo(newOffset) }
            }

            val onMapToggle = {
                scope.launch {
                    val next = when(stage) {
                        SheetStage.Collapsed, SheetStage.HalfExpanded -> SheetStage.Expanded
                        SheetStage.Expanded -> SheetStage.Collapsed
                    }
                    animateTo(next)
                }
            }

            Box(Modifier.fillMaxSize()) {
                //MapSearch(viewModel = viewModel)

                SearchHeader(
                    modifier = Modifier
                        .statusBarsPadding()
                        .onGloballyPositioned { coords ->
                            val measured = with(density) { coords.size.height.toDp() }
                            headerHeightDp = (measured + innerPadding.calculateTopPadding())
                                .coerceAtLeast(0.dp)
                        },
                    headline = stringResource(R.string.allServices),
                    subHeadline = stringResource(R.string.anytimeAnyHour),
                    sheetValue = stage,
                    onMapToggle = onMapToggle
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = headerHeightDp + BasePadding)
                        .height(availableHeightDp - BasePadding)
                        .offset { IntOffset(x = 0, y = offset.value.roundToInt()) }
                        .background(
                            color = Background,
                            shape = RoundedCornerShape(
                                topStart = BasePadding,
                                topEnd = BasePadding
                            )
                        )
                        .draggable(
                            orientation = Orientation.Vertical,
                            state = dragState,
                            onDragStopped = { velocity ->
                                val current = offset.value

                                val candidates = listOf(
                                    SheetStage.Collapsed,
                                    SheetStage.HalfExpanded,
                                    SheetStage.Expanded
                                )

                                val nearest = candidates
                                    .map { st -> st to st.targetOffsetPx(availableHeightPx, collapsedHeightPx) }
                                    .minBy { (_, px) -> abs(px - current) }
                                val idx = candidates.indexOf(nearest.first)

                                val shouldGoNext = velocity < -500 && idx < candidates.lastIndex
                                val shouldGoBack = velocity > 500 && idx > 0

                                val target = when {
                                    shouldGoNext -> candidates[idx + 1]
                                    shouldGoBack ->candidates[idx - 1]
                                    else -> nearest.first
                                }

                                scope.launch {
                                    animateTo(target)
                                }
                            }
                        )

                ) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        SearchSheetHeader(
                            onMeasured = { collapsedHeightDp = it + BasePadding },
                            onClick = {
                                scope.launch { sheetState.show() }
                            }
                        )

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
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            userScrollEnabled = stage == SheetStage.Expanded,
                            contentPadding = PaddingValues(bottom = BasePadding),
                            overscrollEffect = null
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
                }
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