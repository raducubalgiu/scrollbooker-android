package com.example.scrollbooker.ui.search.businessProfile.tabs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.scrollbooker.R
import com.example.scrollbooker.components.customized.SchedulesSection
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.entity.booking.schedule.domain.model.Schedule
import com.example.scrollbooker.ui.shared.location.LocationSection
import com.example.scrollbooker.ui.theme.headlineMedium
import com.example.scrollbooker.ui.theme.headlineSmall

@Composable
fun BusinessAboutTab() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier.padding(
                horizontal = BasePadding,
                vertical = SpacingXL
            ),
            text = stringResource(R.string.about),
            style = headlineSmall,
            fontWeight = FontWeight.SemiBold
        )

        Text(
            modifier = Modifier.padding(horizontal = BasePadding),
            text = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.")

        Spacer(Modifier.height(SpacingXL))

        Text(
            modifier = Modifier.padding(horizontal = BasePadding),
            text = stringResource(R.string.schedule),
            style = headlineMedium,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(Modifier.height(SpacingXL))

        SchedulesSection(
            modifier = Modifier.padding(horizontal = BasePadding),
            schedules = listOf(
                Schedule(
                    id = 1,
                    dayOfWeek = "Monday",
                    startTime = "09:00:00",
                    endTime = "18:00:00"
                ),
                Schedule(
                    id = 1,
                    dayOfWeek = "Thursday",
                    startTime = "09:00:00",
                    endTime = "18:00:00"
                ),
                Schedule(
                    id = 1,
                    dayOfWeek = "Wednesday",
                    startTime = "09:00:00",
                    endTime = "18:00:00"
                ),
                Schedule(
                    id = 1,
                    dayOfWeek = "Thursday",
                    startTime = "09:00:00",
                    endTime = "18:00:00"
                ),
                Schedule(
                    id = 1,
                    dayOfWeek = "Friday",
                    startTime = "09:00:00",
                    endTime = "18:00:00"
                ),
                Schedule(
                    id = 1,
                    dayOfWeek = "Saturday",
                    startTime = null,
                    endTime = null
                ),
                Schedule(
                    id = 1,
                    dayOfWeek = "Sunday",
                    startTime = null,
                    endTime = null
                )
            )
        )

        Text(
            modifier = Modifier.padding(horizontal = BasePadding),
            text = stringResource(R.string.location),
            style = headlineSmall,
            fontWeight = FontWeight.SemiBold
        )

        LocationSection(
            modifier = Modifier.padding(horizontal = BasePadding)
        )

        Spacer(Modifier.height(SpacingXL))
    }
}