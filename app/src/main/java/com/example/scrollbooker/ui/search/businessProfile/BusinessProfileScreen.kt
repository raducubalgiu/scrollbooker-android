package com.example.scrollbooker.ui.search.businessProfile
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.ui.search.businessProfile.tabs.BusinessAboutTab
import com.example.scrollbooker.ui.search.businessProfile.tabs.BusinessEmployeesTab
import com.example.scrollbooker.ui.search.businessProfile.tabs.BusinessPhotosTab
import com.example.scrollbooker.ui.search.businessProfile.tabs.BusinessReviewsTab
import com.example.scrollbooker.ui.search.businessProfile.tabs.BusinessServicesTab
import com.example.scrollbooker.ui.search.businessProfile.tabs.BusinessSocialTab
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.titleMedium
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun BusinessProfileScreen(onBack: () -> Unit) {
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

    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(400)
        isLoading = false
    }

    if(!isLoading) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Background)
        ) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(imageHeight)
                .graphicsLayer {
                    alpha = imageAlpha
                    translationY = imageTranslationY
                }
                .zIndex(2f)
            ) {
                AsyncImage(
                    model = "https://media.scrollbooker.ro/frizeria-figaro-location-1.avif",
                    contentDescription = "Business gallery",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .height(imageHeight)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(8.dp)
                    .size(36.dp)
                    .zIndex(3f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Background)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null
                    )
                }

                AnimatedVisibility(
                    visible = imageAlpha == 0f,
                    enter = fadeIn(tween(250)),
                    exit = fadeOut(tween(250))
                ) {
                    Text(
                        text = "House Of Barbers",
                        style = titleMedium,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }

                IconButton(
                    onClick = {},
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Background)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_star_outline),
                        contentDescription = null
                    )
                }
            }

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

                item(key = BusinessProfileSection.Photos.key) {
                    BusinessPhotosTab(
                        modifier = Modifier.padding(
                            top = imageHeight - overlayHeight,
                            bottom = BasePadding
                        )
                    )
                }
                item(key = BusinessProfileSection.Services.key) { BusinessServicesTab() }
                item(key = BusinessProfileSection.Social.key) { BusinessSocialTab() }
                item(key = BusinessProfileSection.Employees.key) { BusinessEmployeesTab() }
                item(key = BusinessProfileSection.Reviews.key) { BusinessReviewsTab() }
                item(key = BusinessProfileSection.About.key) { BusinessAboutTab() }
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
    } else {
        LoadingScreen()
    }
}