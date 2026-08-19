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
import com.example.scrollbooker.components.customized.stats.StatBarRow
import com.example.scrollbooker.components.customized.stats.StatTitle
import com.example.scrollbooker.core.enums.BookingSourceEnum
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.ui.theme.Background

@Composable
fun MyDashboardBookingSource(modifier: Modifier = Modifier) {
    val dummyAppointmentSources = listOf(
        AppointmentSource(source = BookingSourceEnum.PROFILE, value = "150", progress = 1.0f),
        AppointmentSource(source = BookingSourceEnum.BOOK_AGAIN, value = "98", progress = 0.65f),
        AppointmentSource(source = BookingSourceEnum.SEARCH, value = "74", progress = 0.49f),
        AppointmentSource(source = BookingSourceEnum.EXPLORE_FEED, value = "45", progress = 0.30f),
        AppointmentSource(source = BookingSourceEnum.PROFILE_GRID_POST_DETAIL, value = "30", progress = 0.20f),
        AppointmentSource(source = BookingSourceEnum.FOLLOWING_FEED, value = "22", progress = 0.14f),
        AppointmentSource(source = BookingSourceEnum.SEARCH_BUSINESS_PROFILE, value = "12", progress = 0.08f),
        AppointmentSource(source = BookingSourceEnum.PROFILE_BOOKMARKS_POST_DETAIL, value = "5", progress = 0.03f)
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(ShapeDefaults.Medium)
            .background(Background)
            .padding(BasePadding)
    ) {
        StatTitle(
            modifier = Modifier.padding(bottom = SpacingM),
            title = "Surse rezervari",
            onClick = {}
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(SpacingM),
            modifier = Modifier.fillMaxWidth()
        ) {
            dummyAppointmentSources.forEach { appointmentSource ->
                StatBarRow(
                    label = appointmentSource.source.key,
                    valueString = appointmentSource.value,
                    progressPercentage = appointmentSource.progress
                )
            }
        }
    }
}