package com.example.scrollbooker.ui.booking.services
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.components.core.tabs.ServiceTab
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.entity.booking.products.domain.model.BusinessServicesWithProducts
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider

@Composable
fun BookingServicesTabs(
    activeTabIndexProvider: () -> Int,
    edgePadding: Dp = BasePadding,
    onTabChange: (Int) -> Unit,
    serviceGroups:  List<BusinessServicesWithProducts>
) {
    ScrollableTabRow(
        selectedTabIndex = activeTabIndexProvider().coerceIn(0, serviceGroups.lastIndex),
        edgePadding = edgePadding,
        containerColor = Background,
        divider = {},
        indicator = { _ -> Box(Modifier.size(0.dp)) },
        modifier = Modifier.fillMaxWidth()
    ) {
        serviceGroups.forEachIndexed { index, group ->
            val isSelected = activeTabIndexProvider() == index

            ServiceTab(
                isSelected = isSelected,
                serviceName = group.service.shortName,
                onClick = { onTabChange(index) },
            )
        }
    }

    HorizontalDivider(
        color = Divider,
        thickness = 0.55.dp
    )
}