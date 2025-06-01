package com.example.scrollbooker.feature.profile.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import kotlinx.coroutines.launch

class ProfileTab(
    val route: String,
    val icon: Int
)

@Composable
fun ProfileTabs() {
    val pagerState = rememberPagerState(initialPage = 0) { 4 }
    val selectedTabIndex = pagerState.currentPage

    val tabs = listOf(
        ProfileTab(route = "Posts", icon = R.drawable.ic_grid),
        ProfileTab(route = "Products", icon = R.drawable.ic_shop),
        ProfileTab(route = "Bookmarks", icon = R.drawable.ic_bookmark),
        ProfileTab(route = "Info", icon = R.drawable.ic_info)
    )

    TabRow(
        containerColor = Background,
        contentColor = OnSurfaceBG,
        indicator = {  tabPositions ->
            Box(
                Modifier
                    .tabIndicatorOffset(tabPositions[selectedTabIndex])
                    .height(1.5.dp)
                    .background(OnBackground)
            )
        },
        selectedTabIndex = selectedTabIndex
    ) {
        val coroutineScope = rememberCoroutineScope()

        tabs.forEachIndexed { index, item ->
            val isSelected = selectedTabIndex == index

            Tab(
                selected = isSelected,
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = null,
                        tint = if(isSelected) OnBackground else Color.Gray
                    )
                }
            )
        }
    }

    //            item {
//                HorizontalPager(
//                    state = pagerState,
//                    modifier = Modifier.fillMaxWidth().heightIn(min = 500.dp)
//                ) { page ->
//                    when(page) {
//                        0 -> AppointmentsBusinessTab()
//                        1 -> AppointmentsClientTab()
//                        2 -> Column(Modifier.fillMaxSize()) {}
//                        3 -> Column(Modifier.fillMaxSize()) {  }
//                    }
//                }
//            }
}