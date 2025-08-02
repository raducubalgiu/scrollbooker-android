package com.example.scrollbooker.ui.feed.components.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.components.core.tabs.StyledTab
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.OnSurfaceBG

@Composable
fun FeedSearchResultsTabRow(
    selectedTabIndex: Int,
    tabs: List<String>,
    onChangeTab: (Int) -> Unit
) {
    ScrollableTabRow(
        containerColor = Background,
        contentColor = OnSurfaceBG,
        edgePadding = BasePadding,
        selectedTabIndex = selectedTabIndex,
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
        tabs.forEachIndexed { index, title ->
            val isSelected = selectedTabIndex == index

            StyledTab(
                title = title,
                isSelected = isSelected,
                onClick = { onChangeTab(index) },
            )
        }
    }
}