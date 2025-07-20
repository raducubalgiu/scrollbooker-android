package com.example.scrollbooker.screens.feed.search
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXS
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.bodyLarge
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue

@Composable
fun FeedSearchResultsScreen(
    viewModel: FeedSearchViewModel,
    onBack: () -> Unit
) {
    val tabs = listOf(
        "For You",
        "Users",
        "Last Minute",
        "Instant Booking",
        "Servicii",
        "Reviews"
    )

    val pagerState = rememberPagerState(initialPage = 0 ) { 6 }
    val selectedTabIndex = pagerState.currentPage
    val coroutineScope = rememberCoroutineScope()

    val search by viewModel.currentSearch.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .statusBarsPadding()
    )  {
        FeedSearchHeader(
            value = search,
            onValueChange = {},
            readOnly = true,
            onSearch = {},
            onClearInput = onBack,
            onClick = onBack,
            onBack = onBack,
        )

        Spacer(Modifier.height(SpacingXS))

        ScrollableTabRow(
            containerColor = Background,
            contentColor = OnSurfaceBG,
            edgePadding = BasePadding,
            selectedTabIndex = pagerState.currentPage,
            indicator = {  tabPositions ->
                Box(
                    Modifier
                        .tabIndicatorOffset(tabPositions[selectedTabIndex])
                        .height(3.5.dp)
                        .padding(horizontal = 20.dp)
                        .background(
                            color = OnBackground,
                            shape = ShapeDefaults.Large
                        )
                )
            },
            divider = { HorizontalDivider(color = Divider, thickness = 0.55.dp) },
        ) {
            tabs.forEachIndexed { index, tab ->
                val isSelected = selectedTabIndex == index

                Box(modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                vertical = 16.dp,
                                horizontal = 14.dp
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = tab,
                            style = bodyLarge,
                            fontSize = 16.sp,
                            color = if (isSelected) OnSurfaceBG else Color.Gray,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }
        }

        HorizontalPager(
            state = pagerState,
            beyondViewportPageCount = 0,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            when(page) {
                0 -> Box(Modifier.fillMaxSize()) {
                    Text("For You")
                }
                1 -> Box(Modifier.fillMaxSize()) {
                    Text("Users")
                }
                2 -> Box(Modifier.fillMaxSize()) {
                    Text("Servicii")
                }
                3 -> Box(Modifier.fillMaxSize()) {
                    Text("Last Minute")
                }
                4 -> Box(Modifier.fillMaxSize()) {
                    Text("Instant Booking")
                }
                5 -> Box(Modifier.fillMaxSize()) {
                    Text("Reviews")
                }
            }
        }
    }
}