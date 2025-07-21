package com.example.scrollbooker.screens.search
import android.annotation.SuppressLint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.nomenclature.businessType.domain.model.BusinessType
import com.example.scrollbooker.screens.search.components.SearchHeader
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.Primary
import com.mapbox.geojson.Point
import com.mapbox.maps.Style
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.annotation.ViewAnnotation
import com.mapbox.maps.extension.compose.style.MapStyle
import com.mapbox.maps.viewannotation.geometry
import com.mapbox.maps.viewannotation.viewAnnotationOptions
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

enum class BottomSheetValue(val fraction: Float) {
    Collapsed(0.4f),
    HalfExpanded(0.7f),
    Expanded(1f)
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("ConfigurationScreenWidthHeight", "UnusedBoxWithConstraintsScope")
@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    businessTypesState: FeatureState<List<BusinessType>>,
    onNavigateToBusinessProfile: () -> Unit
) {
    val isSystemInDarkMode = isSystemInDarkTheme()

    var viewAnnotationList1 by remember { mutableStateOf(
        listOf(
            Point.fromLngLat(26.104, 44.437),
            Point.fromLngLat(25.993856, 44.453898),
            Point.fromLngLat(26.009778, 44.451661),
            Point.fromLngLat(26.117450, 44.429056),
            Point.fromLngLat(26.094708, 44.428457),
            Point.fromLngLat(26.074734, 44.437146),
            Point.fromLngLat(26.064108, 44.432652),
            Point.fromLngLat(26.109086, 44.451537),
            Point.fromLngLat(26.146140, 44.409470),
            Point.fromLngLat(25.991507, 44.444861),
            Point.fromLngLat(25.989817, 44.363089),
        )
    ) }

    val selectedBusinessType by viewModel.selectedBusinessType.collectAsState()
    val mapViewportState = rememberMapViewportState {
        setCameraOptions {
            center(Point.fromLngLat(26.104, 44.437))
            zoom(10.0)
            pitch(60.0)
        }
    }
    val cameraState = mapViewportState.cameraState

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val density = LocalDensity.current
        val totalHeightPx = with(density) { maxHeight.toPx() }

        MapboxMap(
            modifier = Modifier.fillMaxSize(),
            mapViewportState = mapViewportState,
            style = { MapStyle(style = if(isSystemInDarkMode) Style.DARK else Style.STANDARD) },
            scaleBar = {},
        ) {
            viewAnnotationList1.forEachIndexed { index, point ->
                ViewAnnotation(
                    options = viewAnnotationOptions {
                        geometry(point)
                        allowOverlap(true)
                    }
                ) {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        painter = painterResource(R.drawable.ic_location_solid),
                        contentDescription = null,
                        tint = Primary
                    )
                }
            }
        }

        Column(modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
        ) {
            SearchHeader(
                headline = selectedBusinessType?.name ?: stringResource(R.string.allServices),
                subHeadline = stringResource(R.string.anytimeAnyHour)
            )

//            Column {
//                when(val businessTypes = businessTypesState) {
//                    is FeatureState.Success -> {
//                        LazyRow {
//                            item { Spacer(Modifier.width(BasePadding)) }
//
//                            item {
//                                SearchBusinessTypeItem(
//                                    title = "Toate",
//                                    isSelected = selectedBusinessType == null,
//                                    onClick = { viewModel.setBusinessType(null) }
//                                )
//
//                                Spacer(Modifier.width(SpacingS))
//                            }
//
//                            itemsIndexed(businessTypes.data) { index, businessType ->
//                                val isSelected = selectedBusinessType?.id == businessType.id
//
//                                SearchBusinessTypeItem(
//                                    title = businessType.plural,
//                                    isSelected = isSelected,
//                                    onClick = {
//                                        viewModel.setBusinessType(businessType)
//                                    }
//                                )
//
//                                if(index < businessTypesState.data.size) {
//                                    Spacer(Modifier.width(SpacingS))
//                                }
//                            }
//
//                            item { Spacer(Modifier.width(BasePadding)) }
//                        }
//                    }
//                    else -> Unit
//                }
//            }

            Spacer(
                Modifier.height(BasePadding)
            )

            val lazyListState = rememberLazyListState()
            val scope = rememberCoroutineScope()

            val offset = remember {
                Animatable(totalHeightPx * (1f - BottomSheetValue.Collapsed.fraction))
            }

            val minPx = remember(totalHeightPx) {
                totalHeightPx * (1f - BottomSheetValue.Expanded.fraction)
            }
            val maxPx = remember(totalHeightPx) {
                totalHeightPx * (1f - BottomSheetValue.Collapsed.fraction)
            }

            val isAtTop by remember {
                derivedStateOf {
                    lazyListState.firstVisibleItemIndex == 0
                    && lazyListState.firstVisibleItemScrollOffset == 0
                }
            }

            val dragState = rememberDraggableState { delta ->
                val newOffset = (offset.value + delta)
                    .coerceIn(minPx, maxPx)
                scope.launch { offset.snapTo(newOffset) }
            }

            val nestedScrollConnection = remember {
                object: NestedScrollConnection {
                    override fun onPreScroll(
                        available: Offset,
                        source: NestedScrollSource
                    ): Offset {
                        val isScrollingDown = available.y > 0f
                        val isSheetFullyExpanded = offset.value < maxPx

                        return if(isAtTop && isScrollingDown && isSheetFullyExpanded) {
                            scope.launch {
                                val newOffset = (offset.value + available.y).coerceIn(minPx, maxPx)
                                offset.snapTo(newOffset)
                            }
                            Offset(0f, available.y)
                        } else {
                            Offset.Zero
                        }
                    }

                    override fun onPostScroll(
                        consumed: Offset,
                        available: Offset,
                        source: NestedScrollSource
                    ): Offset {
                        return Offset.Zero
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    //.nestedScroll(nestedScrollConnection)
            ) {
                Box(
                    modifier = Modifier
                        .offset { IntOffset(x = 0, y = offset.value.roundToInt()) }
                        .fillMaxWidth()
                        .heightIn(min = 150.dp, max = with(density) { totalHeightPx.toDp() })
                        .background(Color.White, RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                        .draggable(
                            orientation = Orientation.Vertical,
                            state = dragState,
                            onDragStopped = { velocity ->
                                val current = offset.value
                                val nearest = BottomSheetValue.entries
                                    .map { target ->
                                        val targetOffset = totalHeightPx * (1f - target.fraction)
                                        val distance = abs(targetOffset - current)
                                        Triple(target, targetOffset, distance)
                                    }
                                    .minByOrNull { it.third } ?: return@draggable

                                val shouldGoNext = velocity < -1000 && nearest.first.ordinal < BottomSheetValue.entries.lastIndex
                                val shouldGoBack = velocity > 1000 && nearest.first.ordinal > 0

                                val target = when {
                                    shouldGoNext -> BottomSheetValue.entries[nearest.first.ordinal + 1]
                                    shouldGoBack -> BottomSheetValue.entries[nearest.first.ordinal - 1]
                                    else -> nearest.first
                                }
                                val targetOffset = totalHeightPx * (1f - target.fraction)

                                scope.launch {
                                    offset.animateTo(
                                        targetOffset,
                                        animationSpec = tween(200, easing = FastOutSlowInEasing)
                                    )
                                }
                            }
                        )

                ) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .background(Color.Red),
                            contentAlignment = Alignment.TopCenter
                        ) {
                            Box(
                                modifier = Modifier
                                    .padding(top = BasePadding)
                                    .width(60.dp)
                                    .height(4.dp)
                                    .clip(shape = ShapeDefaults.ExtraLarge)
                                    .background(Divider)
                            )
                        }

                        LazyColumn(
                            state = lazyListState,
                            modifier = Modifier.weight(1f),
                            //userScrollEnabled = isAtTop
                        ) {
                            items(100) {
                                Box(Modifier.fillMaxWidth()) {
                                    Text("Text")
                                }
                            }
                        }
                    }
                }

            }
        }
    }
}