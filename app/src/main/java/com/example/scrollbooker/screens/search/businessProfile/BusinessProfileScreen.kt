package com.example.scrollbooker.screens.search.businessProfile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.components.core.headers.Header
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.screens.search.businessProfile.tabs.BusinessAboutTab
import com.example.scrollbooker.screens.search.businessProfile.tabs.BusinessEmployeesTab
import com.example.scrollbooker.screens.search.businessProfile.tabs.BusinessReviewsTab
import com.example.scrollbooker.screens.search.businessProfile.tabs.BusinessServicesTab
import com.example.scrollbooker.screens.search.businessProfile.tabs.BusinessSocialTab
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.bodyLarge
import kotlinx.coroutines.launch

@Composable
fun BusinessProfileScreen(onBack: () -> Unit) {
    val sections = BusinessProfileSection.all
    val itemKeys = sections.map { it.key }

    val lazyListState = rememberLazyListState()
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()
    ) {
        Header(onBack = onBack)
        LazyColumn(
            state = lazyListState,
            modifier = Modifier.fillMaxSize()
        ) {
            stickyHeader {
                Surface(tonalElevation = 4.dp) {
                    ScrollableTabRow(
                        selectedTabIndex = selectedTabIndex,
                        modifier = Modifier
                            .fillMaxWidth(),
                        containerColor = Background,
                        contentColor = OnSurfaceBG,
                        edgePadding = SpacingS,
                        indicator = { tabPositions ->
                            Box(
                                Modifier
                                    .tabIndicatorOffset(tabPositions[selectedTabIndex])
                                    .height(2.5.dp)
                                    .padding(horizontal = 20.dp)
                                    .background(OnBackground)
                            )
                        },
                        divider = { HorizontalDivider(color = Divider, thickness = 0.55.dp) }
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
                                        text = stringResource(section.label),
                                        fontWeight = FontWeight.Bold,
                                        style = bodyLarge
                                    )
                                }
                            )
                        }
                    }
                }
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
}