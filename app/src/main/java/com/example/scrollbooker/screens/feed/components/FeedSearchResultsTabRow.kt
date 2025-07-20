package com.example.scrollbooker.screens.feed.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.bodyLarge

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
        tabs.forEachIndexed { index, tab ->
            val isSelected = selectedTabIndex == index

            Box(modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = { onChangeTab(index) }
                ),
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
}