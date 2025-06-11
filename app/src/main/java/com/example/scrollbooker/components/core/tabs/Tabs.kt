package com.example.scrollbooker.components.core.tabs
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.bodyLarge

@Composable
fun Tabs(
    tabs: List<String>,
    selectedTabIndex: Int,
    onChangeTab: (Int) -> Unit
) {
    TabRow(
        containerColor = Background,
        contentColor = OnSurfaceBG,
        indicator = { tabPositions ->
            Box(
                Modifier
                    .tabIndicatorOffset(tabPositions[selectedTabIndex])
                    .height(1.5.dp)
                    .background(OnBackground)
            )
        },
        selectedTabIndex = selectedTabIndex
    ) {
        tabs.forEachIndexed { index, title ->
            val isSelected = selectedTabIndex == index

            Tab(
                selected = isSelected,
                onClick = { onChangeTab(index) },
                text = {
                    Text(
                        text = title,
                        style = bodyLarge,
                        color = if (isSelected) OnBackground else OnSurfaceBG,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            )
        }
    }
}