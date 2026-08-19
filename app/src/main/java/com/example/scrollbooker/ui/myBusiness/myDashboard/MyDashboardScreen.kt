package com.example.scrollbooker.ui.myBusiness.myDashboard

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.headers.Header
import com.example.scrollbooker.navigation.navigators.ProfileNavigator
import com.example.scrollbooker.ui.myBusiness.myDashboard.tabs.Bookings.MyDashboardBookingsTab
import com.example.scrollbooker.ui.myBusiness.myDashboard.tabs.MyDashboardPostsTab
import com.example.scrollbooker.ui.myBusiness.myDashboard.tabs.MyDashboardTab
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.SurfaceBG
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MyDashboardScreen(
    viewModel: MyDashboardViewModel,
    profileNavigate: ProfileNavigator
) {
    val tabs = MyDashboardTab.getTabs()

    val pagerState = rememberPagerState { tabs.size }
    val coroutineScope = rememberCoroutineScope()

    val containerColor = SurfaceBG
    val contentColor = OnSurfaceBG

    Scaffold(
        topBar = {
            Header(
                title = stringResource(R.string.dashboard),
                onBack = { profileNavigate.back() },
                containerColor = containerColor,
                contentColor = contentColor
            )
        },
        containerColor = containerColor,
        contentColor = contentColor
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                containerColor = containerColor,
                contentColor = contentColor,
                indicator = { tabPositions ->
                    if (pagerState.currentPage < tabPositions.size) {
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                            height = 3.dp,
                            color = contentColor
                        )
                    }
                }
            ) {
                tabs.forEachIndexed { index, tab ->
                    Tab(
                        text = {
                            Text(
                                text = stringResource(tab.labelRes),
                                fontWeight = FontWeight.SemiBold
                            )
                        },
                        selected = pagerState.currentPage == index,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        }
                    )
                }
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                when (page) {
                    0 -> MyDashboardBookingsTab(viewModel)
                    1 -> MyDashboardPostsTab()
                }
            }
        }
    }
}