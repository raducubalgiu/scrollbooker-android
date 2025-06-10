package com.example.scrollbooker.feature.profile.presentation.components.common.tab

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Repeat
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.outlined.ViewComfyAlt
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import kotlinx.coroutines.launch

class ProfileTab(
    val route: String,
    val icon: ImageVector
)

@Composable
fun ProfileTabRow(
    pagerState: PagerState
) {
    val tabs = listOf(
        ProfileTab(route = "Posts", icon = Icons.Outlined.ViewComfyAlt),
        ProfileTab(route = "Products", icon = Icons.Outlined.ShoppingBag),
        ProfileTab(route = "Reposts", icon = Icons.Outlined.Repeat),
        ProfileTab(route = "Bookmarks", icon = Icons.Outlined.BookmarkBorder),
        ProfileTab(route = "Info", icon = Icons.Outlined.LocationOn)
    )
    val selectedTabIndex = pagerState.currentPage
    val coroutineScope = rememberCoroutineScope()

    TabRow(
        containerColor = Background,
        contentColor = OnSurfaceBG,
        indicator = {  tabPositions ->
            Box(
                Modifier
                    .tabIndicatorOffset(tabPositions[selectedTabIndex])
                    .height(1.5.dp)
                    .padding(horizontal = 10.dp)
                    .background(OnBackground)
            )
        },
        selectedTabIndex = selectedTabIndex
    ) {
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
                    BadgedBox(
                        badge = {
                            if (index == 1) {
                                Box(
                                    Modifier
                                        .offset(x = 15.dp, y = (-12).dp),
                                    contentAlignment = Alignment.TopStart
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .width(9.dp)
                                            .height(9.dp)
                                            .clip(CircleShape)
                                            .background(Error)
                                    )
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = null,
                            tint = if (isSelected) OnBackground else Color.Gray
                        )
                    }
                }
            )
        }
    }
}