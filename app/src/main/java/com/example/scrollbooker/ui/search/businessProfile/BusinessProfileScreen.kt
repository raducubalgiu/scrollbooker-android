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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.scrollbooker.components.core.headers.Header
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.ui.search.businessProfile.components.BusinessProfileHeader
import com.example.scrollbooker.ui.search.businessProfile.components.BusinessProfileSkeleton
import com.example.scrollbooker.ui.search.businessProfile.components.BusinessProfileTabRow
import com.example.scrollbooker.ui.search.businessProfile.sections.about.BusinessAboutSection
import com.example.scrollbooker.ui.search.businessProfile.sections.employees.BusinessEmployeesSection
import com.example.scrollbooker.ui.search.businessProfile.sections.BusinessProfileSection
import com.example.scrollbooker.ui.search.businessProfile.sections.posts.BusinessPostsSection
import com.example.scrollbooker.ui.search.businessProfile.sections.reviews.BusinessReviewsSection
import com.example.scrollbooker.ui.search.businessProfile.sections.services.BusinessServicesSection
import com.example.scrollbooker.ui.search.businessProfile.sections.summary.BusinessSummarySection
import com.example.scrollbooker.ui.theme.Background
import kotlinx.coroutines.launch

@Composable
fun BusinessProfileScreen(
    viewModel: BusinessProfileViewModel,
    onNavigateToUserProfile: (Int) -> Unit,
    onBack: () -> Unit
) {
    val state by viewModel.businessProfileState.collectAsState()
    val isFollow by viewModel.isFollowState.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()

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

            val sections = remember(employees) {
                BusinessProfileSection.getSections(employees)
            }
            val itemKeys = sections.map { it.key }

            val density = LocalDensity.current
            val lazyListState = rememberLazyListState()

            var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }
            val scope = rememberCoroutineScope()

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
                            .fillMaxWidth()
                            .zIndex(3f)
                        )

                        BusinessProfileTabRow(
                            selectedTabIndex = selectedTabIndex,
                            sections = sections,
                            tabRowHeight = tabRowHeight,
                            animatedAlpha = animatedAlpha,
                            onChangeTab = { index, section ->
                                val targetKey = section.key
                                val targetIndex = lazyListState.layoutInfo
                                    .visibleItemsInfo
                                    .find { it.key == targetKey }
                                    ?.index
                                    ?: (itemKeys.indexOf(targetKey) + 1)
                                selectedTabIndex = index

                                scope.launch { lazyListState.animateScrollToItem(targetIndex) }
                            }
                        )
                    }

                    item(key = BusinessProfileSection.Summary.key) {
                        BusinessSummarySection(
                            owner = profile.owner,
                            businessPlan = profile.businessPlan,
                            address = profile.address,
                            distance = profile.distanceKm,
                            openingHours = profile.openingHours,
                            onNavigateToOwnerProfile = onNavigateToUserProfile,
                            onFlyToReviewsSection = {},
                            isFollow = isFollow,
                            isFollowEnabled = !isSaving,
                            onFollow = { viewModel.follow() },
                            modifier = Modifier.padding(
                                top = imageHeight - overlayHeight,
                                bottom = BasePadding
                            )
                        )
                    }

                    item(key = BusinessProfileSection.Services.key) {
                        BusinessServicesSection()
                    }

                    item(key = BusinessProfileSection.Social.key) {
                        BusinessPostsSection()
                    }

                    if(employees.isNotEmpty()) {
                        item(key = BusinessProfileSection.Employees.key) {
                            BusinessEmployeesSection(
                                employees = employees,
                                onNavigateToEmployeeProfile = onNavigateToUserProfile
                            )
                        }
                    }

                    item(key = BusinessProfileSection.Reviews.key) {
                        BusinessReviewsSection(
                            reviews = profile.reviews,
                            ratingsAverage = profile.owner.counters.ratingsAverage,
                            ratingsCount = profile.owner.counters.ratingsCount,
                            onNavigateToReviewerProfile = onNavigateToUserProfile
                        )
                    }

                    item(key = BusinessProfileSection.About.key) {
                        BusinessAboutSection(
                            description = profile.description ?: "",
                            schedules = profile.schedules
                        )
                    }
                }
            }
        }
    }
}