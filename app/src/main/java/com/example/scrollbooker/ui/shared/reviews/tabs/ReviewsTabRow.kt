package com.example.scrollbooker.ui.shared.reviews.tabs

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.OnPrimary
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.bodyMedium

@Composable
fun ReviewsTabRow(
    tabs: List<String>,
    selectedTab: Int,
    onChangeTab: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Background)
            .padding(BasePadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        tabs.mapIndexed { index, tab ->
            val isSelected = selectedTab == index

            Surface(
                shape = RoundedCornerShape(50),
                color = if(isSelected) Primary else Background,
                modifier = Modifier
                    .padding(horizontal = 2.dp)
                    .animateContentSize(),
                onClick = { onChangeTab(index) }
            ) {
                Text(
                    text = tab,
                    style = bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = if(isSelected) OnPrimary else OnBackground,
                    modifier = Modifier
                        .padding(horizontal = 15.dp, vertical = 11.dp)
                )
            }
        }
    }
}