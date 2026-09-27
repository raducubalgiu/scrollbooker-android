package com.example.scrollbooker.ui.feed.components
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.icon.ShadowedIcon
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.navigation.bottomBar.BulletBadge

@Composable
fun FeedTabs(
    modifier: Modifier = Modifier,
    selectedTabIndex: Int,
    activeFiltersCount: Int = 0,
    onOpenDrawer: () -> Unit,
    onNavigateSearch: () -> Unit,
    onChangeTab: (Int) -> Unit,
) {
    val tabs = listOf(
        stringResource(R.string.explore),
        stringResource(R.string.following)
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .zIndex(2f)
    ) {
        Row(
            modifier = Modifier.align(Alignment.Center),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            tabs.forEachIndexed { index, title ->
                FeedTab(
                    isSelected = selectedTabIndex == index,
                    onClick = { onChangeTab(index) },
                    title = title
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(modifier = Modifier.clickable(onClick = onOpenDrawer)) {
                BadgedBox(
                    modifier = Modifier.padding(BasePadding),
                    badge = {
                        if (activeFiltersCount > 0) {
                            BulletBadge()
                        }
                    }
                ) {
                    ShadowedIcon(
                        painter = painterResource(R.drawable.ic_menu_solid),
                        contentDescription = "Menu",
                        iconTintColor = Color(0xFFE0E0E0)
                    )
                }
            }

            Box(modifier = Modifier.clickable { onNavigateSearch() }) {
                Box(
                    modifier = Modifier.padding(BasePadding),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        modifier = Modifier.size(27.5.dp),
                        painter = painterResource(R.drawable.ic_search),
                        contentDescription = null,
                        tint = Color(0xFFE0E0E0),
                    )
                }
            }
        }
    }
}
