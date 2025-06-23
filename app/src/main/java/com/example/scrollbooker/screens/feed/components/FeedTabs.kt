package com.example.scrollbooker.screens.feed.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.scrollbooker.core.util.Dimens.BasePadding

@Composable
fun FeedTabs(
    selectedTabIndex: Int,
    onOpenDrawer: () -> Unit,
    onChangeTab: (Int) -> Unit
) {
    val tabs = listOf("Book Now", "Urmaresti")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .zIndex(2f),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(modifier = Modifier.clickable(onClick = onOpenDrawer)) {
            Box(
                modifier = Modifier.padding(BasePadding),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(30.dp),
                    imageVector = Icons.Outlined.Menu,
                    contentDescription = null,
                    tint = Color(0xFFE0E0E0)
                )
            }
        }

        TabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier.width(200.dp),
            containerColor = Color.Transparent,
            indicator = { tabPositions ->
                val currentTab = tabPositions[selectedTabIndex]

                Box(
                    Modifier
                        .tabIndicatorOffset(currentTab)
                        .padding(horizontal = 40.dp)
                        .height(3.dp)
                        .background(Color.White)
                )
            },
            divider = {}
        ) {
            tabs.forEachIndexed { index, title ->
                val isSelected = selectedTabIndex == index

                val animatedScale by animateFloatAsState(
                    targetValue = if(isSelected) 1.1f else 1f,
                    animationSpec = tween(durationMillis = 300)
                )

                Box(modifier = Modifier
                    .padding(vertical = BasePadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        modifier = Modifier.scale(animatedScale),
                        text = title,
                        color = if (isSelected) Color.White else Color.Gray,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        style = TextStyle(
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp,
                            lineHeight = 24.sp,
                            letterSpacing = 0.5.sp,
                            shadow = Shadow(
                                color = Color.Black.copy(alpha = 0.8f),
                                offset = Offset(2f, 2f),
                                blurRadius = 4f
                            )
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }


        Box(modifier = Modifier.clickable(onClick = {})) {
            Box(
                modifier = Modifier.padding(BasePadding),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(30.dp),
                    imageVector = Icons.Outlined.Search,
                    contentDescription = null,
                    tint = Color(0xFFE0E0E0),
                )
            }
        }
    }
}