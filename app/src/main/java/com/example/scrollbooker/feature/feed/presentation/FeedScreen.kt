package com.example.scrollbooker.feature.feed.presentation
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.feature.appointments.presentation.tabs.business.AppointmentsBusinessTab
import com.example.scrollbooker.feature.appointments.presentation.tabs.client.AppointmentsClientTab
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.titleLarge
import com.example.scrollbooker.ui.theme.titleMedium
import kotlinx.coroutines.launch

@Composable
fun FeedScreen(
    onOpenDrawer: () -> Unit
) {
    val pagerState = rememberPagerState(initialPage = 0) { 2 }
    val selectedTabIndex = pagerState.currentPage
    val tabs = listOf("Book Now", "Urmărești")

    Column(Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = BasePadding, vertical = SpacingM),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier
                .size(30.dp)
                .clickable(
                    onClick = onOpenDrawer,
                ),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_menu),
                    contentDescription = null,
                    tint = OnBackground,
                    modifier = Modifier.size(30.dp)
                )
            }
            TabRow(
                modifier = Modifier.width(200.dp),
                selectedTabIndex = selectedTabIndex,
                containerColor = Color.Transparent,
                indicator = { tabPositions ->
                    val currentTab = tabPositions[selectedTabIndex]

                    Box(
                        Modifier
                            .tabIndicatorOffset(currentTab)
                            //.offset(y = 4.dp)
                            .width(currentTab.width / 2)
                            .height(3.dp)
                            .background(OnBackground)
                    )
                },
                divider = {},
            ) {
                val coroutineScope = rememberCoroutineScope()

                tabs.forEachIndexed { index, title ->
                    val isSelected = selectedTabIndex == index
                    val alignment = Alignment.Center

                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = SpacingS)
                        .background(Color.Transparent)
                        .clickable(onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        }),
                        contentAlignment = alignment
                    ) {
                        Text(
                            text = title,
                            style = titleMedium,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = if(isSelected) OnBackground else OnSurfaceBG
                        )
                    }
                }
            }
            Box(modifier = Modifier
                .size(30.dp)
                .clickable(
                    onClick = {},
                ),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_search),
                    contentDescription = null,
                    tint = OnBackground,
                    modifier = Modifier.size(30.dp)
                )
            }
        }

        HorizontalPager(state = pagerState) { page ->
            when(page) {
                0 -> Column(
                    Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "Book Now", style = titleLarge)
                }
                1 -> Column(
                    Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "Following", style = titleLarge)
                }
            }
        }
    }
}

