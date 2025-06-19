package com.example.scrollbooker.screens.feed.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.scrollbooker.components.BoxIcon
import com.example.scrollbooker.ui.theme.OnBackground

@Composable
fun FeedTabs(
    selectedTabIndex: Int,
    onOpenDrawer: () -> Unit,
    onChangeTab: (Int) -> Unit
) {
    val tabs = listOf("Book Now", "Following")
    val BackgroundLight = Color(0xFFFDFDFD)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .zIndex(2f),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BoxIcon(
            onClick = onOpenDrawer,
            icon = Icons.Default.Search,
            contentAlignment = Alignment.CenterEnd,
            tint = BackgroundLight
        )
        TabRow(
            modifier = Modifier.width(200.dp),
            selectedTabIndex = selectedTabIndex,
            containerColor = Color.Transparent,
            indicator = { tabPositions ->
                val currentTab = tabPositions[selectedTabIndex]

                Column(verticalArrangement = Arrangement.Bottom) {
                    Box(
                        Modifier
                            .tabIndicatorOffset(currentTab)
                            .padding(horizontal = 30.dp)
                            .height(4.dp)
                            .background(OnBackground)
                    )
                }
            },
            divider = {},
        ) {
            tabs.forEachIndexed { index, title ->
                val isSelected = selectedTabIndex == index
                val color = if(isSelected) BackgroundLight else Color(0xFFAAAAAA)

                Box(modifier = Modifier
                    .padding(vertical = 20.dp)
                ) {
                    CustomTab(
                        title = title,
                        color = color,
                        onClick = { onChangeTab(index) },
                    )
                }
            }
        }
        BoxIcon(
            icon = Icons.Outlined.CameraAlt,
            contentAlignment = Alignment.CenterStart,
            onClick = {},
            tint = BackgroundLight
        )
    }
}