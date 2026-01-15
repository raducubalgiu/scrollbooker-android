package com.example.scrollbooker.ui.search.businessProfile
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.scrollbooker.components.core.headers.Header
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.ui.search.SearchViewModel
import com.example.scrollbooker.ui.search.businessProfile.tabs.BusinessAboutTab
import com.example.scrollbooker.ui.search.businessProfile.tabs.BusinessEmployeesTab
import com.example.scrollbooker.ui.search.businessProfile.tabs.BusinessReviewsTab
import com.example.scrollbooker.ui.search.businessProfile.tabs.BusinessServicesTab
import com.example.scrollbooker.ui.search.businessProfile.tabs.BusinessSocialTab
import com.example.scrollbooker.ui.search.businessProfile.tabs.BusinessSummaryTab
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.bodyLarge
import kotlinx.coroutines.launch

@Composable
fun BusinessProfileScreen(
    viewModel: SearchViewModel,
    onBack: () -> Unit
) {
    val state by viewModel.businessProfileState.collectAsState()

    when(val businessProfile = state) {
        is FeatureState.Error -> {
            Box(Modifier.fillMaxSize()) {
                Header(onBack = onBack)
                ErrorScreen()
            }
        }
        is FeatureState.Loading -> BusinessProfileSkeleton()
        is FeatureState.Success -> {
            val profile = businessProfile.data
            val employees = profile.employees

//            val sections = remember(employees) {
//                BusinessProfileSection.getSections(employees)
//            }

            val sections = BusinessProfileSection.all
            val itemKeys = sections.map { it.key }

            val lazyListState = rememberLazyListState()
            var selectedTabIndex by remember { mutableIntStateOf(0) }
            val coroutineScope = rememberCoroutineScope()

            val density = LocalDensity.current

            val insets = WindowInsets.statusBars.asPaddingValues()
            val statusBarHeight = with(density) { insets.calculateTopPadding().toPx() }

            val imageHeight = 250.dp
            val headerHeight = 56.dp + with(density) { statusBarHeight.toDp() }
            val tabRowHeight = 48.dp
            val overlayHeight = headerHeight + tabRowHeight

            val imageAlpha by remember {
                derivedStateOf {
                    val isFirstVisibleItem = lazyListState.firstVisibleItemIndex == 0
                    val offsetPx = lazyListState.firstVisibleItemScrollOffset.toFloat()
                    val maxPx = with(density) { imageHeight.toPx() }

                    if(isFirstVisibleItem) {
                        1f - (offsetPx / maxPx).coerceIn(0f, 1f)
                    } else {
                        0f
                    }
                }
            }

            val imageTranslationY by remember {
                derivedStateOf {
                    val isFirstVisibleItem = lazyListState.firstVisibleItemIndex == 0
                    val offsetPx = lazyListState.firstVisibleItemScrollOffset.toFloat()
                    val maxPx = with(density) { imageHeight.toPx() }

                    if(isFirstVisibleItem) {
                        -offsetPx.coerceIn(0f, maxPx)
                    } else {
                        0f
                    }
                }
            }

            val rawAlpha by remember {
                derivedStateOf {
                    val isFirstVisibleItem = lazyListState.firstVisibleItemIndex == 0
                    val offsetPx = lazyListState.firstVisibleItemScrollOffset.toFloat()
                    val maxPx = with(density) { imageHeight.toPx() }

                    if(isFirstVisibleItem) {
                        (offsetPx / maxPx).coerceIn(0f, 1f)
                    } else {
                        1f
                    }
                }
            }

            val animatedAlpha by animateFloatAsState(
                targetValue = rawAlpha,
                animationSpec = tween(durationMillis = 250),
                label = "TabRowAlpha"
            )

            Box(modifier = Modifier
                .fillMaxSize()
                .background(Background)
            ) {
                BusinessProfileHeader(
                    mediaFiles = profile.mediaFiles,
                    fullName = profile.owner.fullName,
                    onBack = onBack,
                    imageAlpha = imageAlpha,
                    imageHeight = imageHeight,
                    imageTranslationY = imageTranslationY
                )

                LazyColumn(
                    state = lazyListState,
                    modifier = Modifier.fillMaxSize()
                ) {
                    stickyHeader {
                        Spacer(Modifier
                            .height(headerHeight)
                            .background(Background)
                            .fillMaxSize()
                            .zIndex(3f)
                        )
                        Surface(
                            tonalElevation = 4.dp,
                            color = Background
                        ) {
                            ScrollableTabRow(
                                selectedTabIndex = selectedTabIndex,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(tabRowHeight),
                                containerColor = Color.Transparent,
                                contentColor = OnSurfaceBG,
                                edgePadding = SpacingS,
                                indicator = { tabPositions ->
                                    Box(
                                        Modifier
                                            .tabIndicatorOffset(tabPositions[selectedTabIndex])
                                            .height(3.dp)
                                            .padding(horizontal = 20.dp)
                                            .background(
                                                color = OnBackground.copy(alpha = animatedAlpha),
                                                shape = ShapeDefaults.ExtraLarge
                                            )
                                    )
                                },
                                divider = {
                                    HorizontalDivider(
                                        modifier = Modifier.alpha(animatedAlpha),
                                        color = Divider,
                                        thickness = 0.55.dp
                                    )
                                }
                            ) {
                                sections.forEachIndexed { index, section ->
                                    Tab(
                                        selected = selectedTabIndex == index,
                                        onClick = {
                                            coroutineScope.launch {
                                                val targetKey = section.key
                                                val targetIndex = lazyListState.layoutInfo
                                                    .visibleItemsInfo
                                                    .find { it.key == targetKey }
                                                    ?.index
                                                    ?: (itemKeys.indexOf(targetKey) + 1)
                                                lazyListState.animateScrollToItem(targetIndex)

                                                selectedTabIndex = index
                                            }
                                        },
                                        text = {
                                            Text(
                                                modifier = Modifier.alpha(animatedAlpha),
                                                text = stringResource(section.label),
                                                fontWeight = FontWeight.Bold,
                                                style = bodyLarge,
                                                fontSize = 17.sp
                                            )
                                        }
                                    )
                                }
                            }
                        }
                    }

                    item(key = BusinessProfileSection.Summary.key) {
                        BusinessSummaryTab(
                            owner = profile.owner,
                            businessPlan = profile.businessPlan,
                            address = profile.address,
                            distance = profile.distanceKm,
                            openingHours = profile.openingHours,
                            modifier = Modifier.padding(
                                top = imageHeight - overlayHeight,
                                bottom = BasePadding
                            )
                        )
                    }

                    item(key = BusinessProfileSection.Services.key) {
                        BusinessServicesTab()
                    }

                    item(key = BusinessProfileSection.Social.key) {
                        BusinessSocialTab()
                    }

                    if(employees.isNotEmpty()) {
                        item(key = BusinessProfileSection.Employees.key) {
                            BusinessEmployeesTab(employees)
                        }
                    }

                    item(key = BusinessProfileSection.Reviews.key) {
                        BusinessReviewsTab(
                            reviews = profile.reviews,
                            ratingsAverage = profile.owner.counters.ratingsAverage,
                            ratingsCount = profile.owner.counters.ratingsCount
                        )
                    }

                    item(key = BusinessProfileSection.About.key) {
                        BusinessAboutTab(
                            description = profile.description ?: "",
                            schedules = profile.schedules
                        )
                    }
                }

                LaunchedEffect(lazyListState) {
                    snapshotFlow { lazyListState.layoutInfo.visibleItemsInfo }
                        .collect { visibleItems ->
                            val visibleSections = visibleItems.filter { it.key in itemKeys }
                            val topMostSection = visibleSections.minByOrNull { it.offset }
                            val newIndex = topMostSection?.key?.let { itemKeys.indexOf(it) }

                            if(newIndex != null && newIndex != selectedTabIndex) {
                                selectedTabIndex = newIndex
                            }
                        }
                }
            }
        }
    }
}