package com.example.scrollbooker.feature.profile.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.outlined.ViewComfyAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import kotlinx.coroutines.launch

class ProfileTab(
    val route: String,
    val icon: ImageVector
)

@Composable
fun ProfileTabs() {
    val pagerState = rememberPagerState(initialPage = 0) { 4 }
    val selectedTabIndex = pagerState.currentPage

    val tabs = listOf(
        ProfileTab(route = "Posts", icon = Icons.Outlined.ViewComfyAlt),
        ProfileTab(route = "Products", icon = Icons.Outlined.ShoppingBag),
        ProfileTab(route = "Bookmarks", icon = Icons.Outlined.BookmarkBorder),
        ProfileTab(route = "Info", icon = Icons.Outlined.Info)
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
                        imageVector = item.icon,
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