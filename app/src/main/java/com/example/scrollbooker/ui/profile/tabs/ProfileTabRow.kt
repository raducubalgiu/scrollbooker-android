package com.example.scrollbooker.ui.profile.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import kotlinx.coroutines.launch

@Composable
fun ProfileTabRow(
    pagerState: PagerState,
    tabs: List<ProfileTab>,
    onTabRowSizeChanged: (IntSize) -> Unit
) {
    val selectedTabIndex = pagerState.currentPage
    val scope = rememberCoroutineScope()

    TabRow(
        modifier = Modifier
            .fillMaxWidth()
            .onSizeChanged { size ->
                onTabRowSizeChanged(size)
            },
        containerColor = Background,
        contentColor = OnSurfaceBG,
        indicator = {  tabPositions ->
            Box(
                Modifier
                    .tabIndicatorOffset(tabPositions[selectedTabIndex])
                    .height(2.dp)
                    .padding(horizontal = 20.dp)
                    .background(OnBackground, shape = ShapeDefaults.Large)
            )
        },
        divider = { HorizontalDivider(color = Divider, thickness = 0.55.dp) },
        selectedTabIndex = selectedTabIndex
    ) {
        tabs.forEachIndexed { index, tab ->
            val isSelected = selectedTabIndex == index

            Tab(
                selected = isSelected,
                onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
                icon = {
                    Icon(
                        painter = painterResource(tab.icon),
                        contentDescription = null,
                        tint = if (isSelected) OnBackground else Color.Gray
                    )
                }
            )
        }
    }
}