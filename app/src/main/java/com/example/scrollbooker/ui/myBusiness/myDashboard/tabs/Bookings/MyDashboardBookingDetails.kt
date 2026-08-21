package com.example.scrollbooker.ui.myBusiness.myDashboard.tabs.Bookings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.customized.stats.DonutChart
import com.example.scrollbooker.components.customized.stats.DonutChartEntry
import com.example.scrollbooker.components.customized.stats.StatCard
import com.example.scrollbooker.components.customized.stats.StatTitle
import com.example.scrollbooker.core.extensions.toTwoDecimals
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingXXS
import com.example.scrollbooker.entity.dashboard.domain.model.DashboardBooking
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.bodyMedium

@Composable
fun MyDashboardBookingDetails(
    dashboardBooking: DashboardBooking,
    periodText: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(ShapeDefaults.Medium)
            .background(Background)
            .padding(BasePadding)
    ) {
        StatTitle(
            modifier = Modifier.padding(bottom = SpacingXXS),
            title = stringResource(R.string.bookingsDetails),
            onClick = {}
        )

        Text(
            text = periodText,
            style = bodyMedium,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = SpacingM)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(SpacingM)
        ) {
            StatCard(
                modifier = Modifier.weight(1f),
                label = stringResource(R.string.bookings),
                value = dashboardBooking.bookingsNo.toString(),
                containerColor = Color.Transparent,
                contentColor = OnBackground,
                borderColor = Divider
            )
            StatCard(
                modifier = Modifier.weight(1f),
                label = stringResource(R.string.earnings),
                value = "${dashboardBooking.revenue.toTwoDecimals()} RON",
                containerColor = Color.Transparent,
                contentColor = OnBackground,
                borderColor = Divider
            )
        }

        Spacer(Modifier.height(SpacingM))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(SpacingM)
        ) {
            StatCard(
                modifier = Modifier.weight(1f),
                label = stringResource(R.string.finished),
                value = dashboardBooking.finishedBookingsNo.toString(),
                containerColor = Color.Transparent,
                contentColor = OnBackground,
                borderColor = Divider
            )
            StatCard(
                modifier = Modifier.weight(1f),
                label = stringResource(R.string.canceled),
                value = dashboardBooking.cancelledBookingsNo.toString(),
                containerColor = Color.Transparent,
                contentColor = OnBackground,
                borderColor = Divider
            )
        }

        Spacer(Modifier.height(SpacingM))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(SpacingM)
        ) {
            StatCard(
                modifier = Modifier.weight(1f),
                label = stringResource(R.string.fromvideo),
                value = "${dashboardBooking.revenueFromVideo} RON",
                containerColor = Color.Transparent,
                contentColor = OnBackground,
                borderColor = Divider
            )
            StatCard(
                modifier = Modifier.weight(1f),
                label = stringResource(R.string.scrollBookerCommission),
                value = "20 RON",
                contentColor = OnBackground,
                borderColor = Primary.copy(alpha = 0.5f),
                gradientColors = listOf(
                    Primary.copy(alpha = 0.15f),
                    Primary.copy(alpha = 0.02f)
                )
            )
        }

        Spacer(Modifier.height(SpacingM))

        DonutChart(
            title = stringResource(R.string.channel),
            entries = dashboardBooking.channels.map { channel ->
                val channelLabel = channel.channel?.titleRes?.let { stringResource(it) } ?: ""

                DonutChartEntry(
                    label = channelLabel,
                    value = channel.bookingsNo,
                    color = Primary
                )
            }
        )
    }
}