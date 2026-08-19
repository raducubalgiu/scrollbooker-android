package com.example.scrollbooker.ui.myBusiness.myDashboard.tabs.Bookings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ShapeDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.customized.stats.StatBarRow
import com.example.scrollbooker.components.customized.stats.StatTitle
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.entity.dashboard.domain.model.DashboardBookingSource
import com.example.scrollbooker.ui.theme.Background

@Composable
fun MyDashboardBookingSource(
    sources: List<DashboardBookingSource>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(ShapeDefaults.Medium)
            .background(Background)
            .padding(BasePadding)
    ) {
        StatTitle(
            modifier = Modifier.padding(bottom = SpacingM),
            title = stringResource(R.string.bookingsSources),
            onClick = {}
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(SpacingM),
            modifier = Modifier.fillMaxWidth()
        ) {
            sources.forEach { source ->
                StatBarRow(
                    label = source.source?.key ?: "",
                    valueString = source.bookingsNo.toString(),
                    progressPercentage = source.percentage
                )
            }
        }
    }
}