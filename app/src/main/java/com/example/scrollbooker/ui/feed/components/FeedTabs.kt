package com.example.scrollbooker.ui.feed.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.ui.feed.components.search.FeedTab

@Composable
fun FeedTabs(
    selectedTabIndex: Int,
    onOpenDrawer: () -> Unit,
    onNavigateSearch: () -> Unit,
    shouldDisplayBottomBar: Boolean,
    onChangeTab: (Int) -> Unit,
) {
    val tabs = listOf(stringResource(R.string.following), stringResource(R.string.explore))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .zIndex(2f),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Box(modifier = Modifier.clickable(onClick = onOpenDrawer)) {
                Box(
                    modifier = Modifier.padding(BasePadding),
                    contentAlignment = Alignment.Center
                ) {
                    AnimatedContent(
                        targetState = shouldDisplayBottomBar,
                        transitionSpec = { fadeIn(tween(300)) togetherWith fadeOut(tween(300)) },
                        label = "label"
                    ) { display ->
                        Icon(
                            modifier = Modifier.size(30.dp),
                            imageVector = if(display) Icons.Outlined.Menu else Icons.Default.Close ,
                            contentDescription = null,
                            tint = Color(0xFFE0E0E0)
                        )
                    }
                }
            }

            TabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier.width(210.dp),
                containerColor = Color.Transparent,
                indicator = {},
                divider = {}
            ) {
                tabs.forEachIndexed { index, title ->
                    val isSelected = selectedTabIndex == index

                    FeedTab(
                        isSelected = isSelected,
                        onClick = { onChangeTab(index) },
                        title = title
                    )
                }
            }
        }


        Box(modifier = Modifier.clickable { onNavigateSearch() }) {
            Box(
                modifier = Modifier.padding(BasePadding),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(30.dp),
                    painter = painterResource(R.drawable.ic_search),
                    contentDescription = null,
                    tint = Color(0xFFE0E0E0),
                )
            }
        }
    }
}