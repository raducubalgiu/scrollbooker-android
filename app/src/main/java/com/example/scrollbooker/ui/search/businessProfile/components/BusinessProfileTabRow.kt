package com.example.scrollbooker.ui.search.businessProfile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.ui.search.businessProfile.sections.BusinessProfileSection
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.bodyLarge

@Composable
fun BusinessProfileTabRow(
    selectedTabIndex: Int,
    sections: List<BusinessProfileSection>,
    tabRowHeight: Dp,
    animatedAlpha: Float,
    onChangeTab: (Int, BusinessProfileSection) -> Unit
) {
    Surface(
        tonalElevation = 4.dp,
        color = Background
    ) {
        ScrollableTabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier
                .fillMaxWidth()
                .height(tabRowHeight),
            containerColor = Color.Transparent,
            contentColor = OnSurfaceBG,
            edgePadding = SpacingS,
            indicator = { tabPositions ->
                Box(
                    Modifier
                        .tabIndicatorOffset(tabPositions[selectedTabIndex])
                        .height(3.dp)
                        .padding(horizontal = 20.dp)
                        .background(
                            color = OnBackground.copy(alpha = animatedAlpha),
                            shape = ShapeDefaults.ExtraLarge
                        )
                )
            },
            divider = {
                HorizontalDivider(
                    modifier = Modifier.alpha(animatedAlpha),
                    color = Divider,
                    thickness = 0.55.dp
                )
            }
        ) {
            sections.forEachIndexed { index, section ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { onChangeTab(index, section) },
                    text = {
                        Text(
                            modifier = Modifier.alpha(animatedAlpha),
                            text = stringResource(section.label),
                            fontWeight = FontWeight.Bold,
                            style = bodyLarge,
                            fontSize = 17.sp
                        )
                    }
                )
            }
        }
    }
}