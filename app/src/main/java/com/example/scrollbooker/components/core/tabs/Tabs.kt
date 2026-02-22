package com.example.scrollbooker.components.core.tabs
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.OnSurfaceBG

@Composable
fun Tabs(
    tabs: List<String>,
    selectedTabIndex: Int,
    indicatorPadding: Dp = 0.dp,
    onChangeTab: (Int) -> Unit
) {
    TabRow(
        modifier = Modifier.zIndex(14f),
        containerColor = Background,
        contentColor = OnSurfaceBG,
        selectedTabIndex = selectedTabIndex,
        indicator = {  tabPositions ->
            Box(
                Modifier
                    .tabIndicatorOffset(tabPositions[selectedTabIndex])
                    .height(3.5.dp)
                    .padding(horizontal = indicatorPadding)
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
                onClick = { onChangeTab(index) },
                title = title,
                isSelected = isSelected
            )
        }
    }
}