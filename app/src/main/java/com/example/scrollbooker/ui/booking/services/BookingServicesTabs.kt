package com.example.scrollbooker.ui.booking.services

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.entity.booking.products.domain.model.BusinessServicesWithProducts
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.OnPrimary
import com.example.scrollbooker.ui.theme.Primary

@Composable
fun BookingServicesTabs(
    activeTabIndex: Int,
    onTabChange: (Int) -> Unit,
    serviceGroups:  List<BusinessServicesWithProducts>
) {
    ScrollableTabRow(
        selectedTabIndex = activeTabIndex.coerceIn(0, serviceGroups.lastIndex),
        edgePadding = BasePadding,
        containerColor = Background,
        divider = {},
        indicator = { _ -> Box(Modifier.size(0.dp)) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        serviceGroups.forEachIndexed { index, group ->
            val isSelected = activeTabIndex == index

            Tab(
                selected = isSelected,
                onClick = { onTabChange(index) },
                modifier = Modifier
                    .padding(vertical = 6.dp)
                    .clip(ShapeDefaults.ExtraLarge)
                    .background(if (isSelected) Primary else Background),
                text = {
                    val tabTitle = group.service.shortName
                    Text(
                        text = tabTitle,
                        fontSize = 14.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) OnPrimary else OnBackground,
                    )
                }
            )
        }
    }
}