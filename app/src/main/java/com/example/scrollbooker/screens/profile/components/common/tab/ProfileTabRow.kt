package com.example.scrollbooker.screens.profile.components.common.tab

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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import kotlinx.coroutines.launch

class ProfileTab(
    val route: String,
    val icon: Painter
)

@Composable
fun ProfileTabRow(
    pagerState: PagerState
) {
    val tabs = listOf(
        ProfileTab(route = "Posts", painterResource(R.drawable.ic_video_outline)),
        ProfileTab(route = "Products", icon = painterResource(R.drawable.ic_shopping_outline)),
        ProfileTab(route = "Reposts", icon = painterResource(R.drawable.ic_arrow_repeat_outline)),
        ProfileTab(route = "Bookmarks", icon = painterResource(R.drawable.ic_bookmark_outline)),
        ProfileTab(route = "Info", icon = painterResource(R.drawable.ic_location_outline))
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
                    .padding(horizontal = 15.dp)
                    .background(OnBackground, shape = ShapeDefaults.Large)
            )
        },
        divider = { HorizontalDivider(color = Divider, thickness = 0.55.dp) },
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
                                            .background(Color.Green)
                                    )
                                }
                            }
                        }
                    ) {
                        Icon(
                            painter = item.icon,
                            contentDescription = null,
                            tint = if (isSelected) OnBackground else Color.Gray
                        )
                    }
                }
            )
        }
    }
}