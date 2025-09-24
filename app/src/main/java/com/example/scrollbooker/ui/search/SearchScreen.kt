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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.coerceAtMost
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.navigation.bottomBar.MainTab
import com.example.scrollbooker.navigation.routes.MainRoute
import com.example.scrollbooker.ui.search.components.SearchHeader
import com.example.scrollbooker.ui.search.sheet.SearchMap
import com.example.scrollbooker.ui.search.sheet.SearchSheetHeader
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

private val CollapsedHeightDp = 150.dp
private const val HALF_FRACTION = 0.7f
enum class SheetStage { Collapsed, HalfExpanded, Expanded }

fun SheetStage.targetOffsetPx(totalHeightPx: Float, density: Density): Float {
    val collapsedPx = with(density) { CollapsedHeightDp.toPx() }

    return when(this) {
        SheetStage.Collapsed -> totalHeightPx - collapsedPx
        SheetStage.HalfExpanded -> totalHeightPx * (1f - HALF_FRACTION)
        SheetStage.Expanded -> 0f
    }
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onNavigateToBusinessProfile: () -> Unit,
    appointmentsNumber: Int,
    onChangeTab: (MainTab) -> Unit
) {
    val selectedBusinessType by viewModel.selectedBusinessType.collectAsState()

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

            val availableHeightDp = maxHeight - bottomBarPadDp
            val availableHeightPx = with(density) { availableHeightDp.toPx() }

            val collapsedHeightDp = CollapsedHeightDp.coerceAtMost(availableHeightDp)

            var stage by remember { mutableStateOf(SheetStage.Collapsed) }
            val offset = remember {
                Animatable(stage.targetOffsetPx(availableHeightPx, density))
            }

            val scope = rememberCoroutineScope()

            suspend fun animateTo(target: SheetStage) {
                val targetPx = target.targetOffsetPx(availableHeightPx, density)
                offset.stop()
                offset.animateTo(
                    targetPx,
                    animationSpec = tween(200, easing = FastOutSlowInEasing)
                )
                stage = target
            }

            val minPx = SheetStage.Expanded.targetOffsetPx(availableHeightPx, density)
            val maxPx = SheetStage.Collapsed.targetOffsetPx(availableHeightPx, density)

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
                SearchMap()

                SearchHeader(
                    headline = selectedBusinessType?.name ?: stringResource(R.string.allServices),
                    subHeadline = stringResource(R.string.anytimeAnyHour),
                    sheetValue = stage,
                    onMapToggle = onMapToggle
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(availableHeightDp)
                        .offset { IntOffset(x = 0, y = offset.value.roundToInt()) }
                        .background(
                            color = Color.White,
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

                                val triples = candidates.map { st ->
                                    val px = st.targetOffsetPx(availableHeightPx, density)
                                    Triple(st, px, abs(px - current))
                                }

                                val nearest = triples.minByOrNull { it.third } ?: return@draggable

                                val idx = candidates.indexOf(nearest.first)

                                val shouldGoNext = velocity < -1000 && idx < candidates.lastIndex
                                val shouldGoBack = velocity > 1000 && idx > 0

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
                        SearchSheetHeader(collapsedHeight = collapsedHeightDp)

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            userScrollEnabled = stage == SheetStage.Expanded,
                            contentPadding = PaddingValues(bottom = BasePadding)
                        ) {
                            items(100) {
                                Box(Modifier.fillMaxWidth()) {
                                    Text("BusinessProfile")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}