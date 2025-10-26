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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.UnstableApi
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.ui.search.components.MapSearch
import com.example.scrollbooker.ui.search.components.SearchGoogleMap
import com.example.scrollbooker.ui.search.components.SearchHeader
import com.example.scrollbooker.ui.search.components.SearchResultsList
import com.example.scrollbooker.ui.search.components.SearchSheetHeader
import com.example.scrollbooker.ui.search.sheets.SearchSheetActionEnum
import com.example.scrollbooker.ui.search.sheets.SearchSheets
import com.example.scrollbooker.ui.search.sheets.SearchSheetsContent
import com.example.scrollbooker.ui.search.sheets.SearchSheetsContent.PriceSheet
import com.example.scrollbooker.ui.search.sheets.SearchSheetsContent.RatingSheet
import com.example.scrollbooker.ui.search.sheets.SearchSheetsContent.ServicesSheet
import com.example.scrollbooker.ui.search.sheets.SearchSheetsContent.SortSheet
import com.example.scrollbooker.ui.theme.Background
import kotlinx.coroutines.launch
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

@androidx.annotation.OptIn(UnstableApi::class)
@SuppressLint("UnusedBoxWithConstraintsScope")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onNavigateToBusinessProfile: () -> Unit
) {
    //val isMapLoaded by viewModel.isMapLoaded.collectAsState()
    val scope = rememberCoroutineScope()
    var stage by rememberSaveable { mutableStateOf(SheetStage.HalfExpanded) }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var sheetContent by remember { mutableStateOf<SearchSheetsContent>(SearchSheetsContent.None) }

    if(sheetState.isVisible) {
        key(sheetContent) {
            SearchSheets(
                sheetState = sheetState,
                sheetContent = sheetContent,
                onClose = {
                    scope.launch {
                        sheetState.hide()
                        sheetContent = SearchSheetsContent.None
                    }
                },
            )
        }
    }

    fun handleOpenSheet(targetSheet: SearchSheetsContent) {
        scope.launch {
            sheetState.show()
            sheetContent = targetSheet
        }
    }

    Scaffold(
        bottomBar = { BottomBar() }
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
                MapSearch(viewModel = viewModel)
//                SearchGoogleMap(
//                    viewModel = viewModel
//                )

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
                                    .map { st ->
                                        st to st.targetOffsetPx(
                                            availableHeightPx,
                                            collapsedHeightPx
                                        )
                                    }
                                    .minBy { (_, px) -> abs(px - current) }
                                val idx = candidates.indexOf(nearest.first)

                                val shouldGoNext = velocity < -500 && idx < candidates.lastIndex
                                val shouldGoBack = velocity > 500 && idx > 0

                                val target = when {
                                    shouldGoNext -> candidates[idx + 1]
                                    shouldGoBack -> candidates[idx - 1]
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
                            onAction = { action ->
                                when(action) {
                                    SearchSheetActionEnum.OPEN_SERVICES -> {
                                        handleOpenSheet(ServicesSheet(userId = 1))
                                    }
                                    SearchSheetActionEnum.OPEN_PRICE -> {
                                        handleOpenSheet(PriceSheet(userId = 1))
                                    }
                                    SearchSheetActionEnum.OPEN_SORT -> {
                                        handleOpenSheet(SortSheet(1))
                                    }
                                    SearchSheetActionEnum.OPEN_RATINGS -> {
                                        handleOpenSheet(RatingSheet(userId = 1))
                                    }
                                }
                            },
                        )

                        SearchResultsList()
                    }
                }
            }
        }
    }
}

