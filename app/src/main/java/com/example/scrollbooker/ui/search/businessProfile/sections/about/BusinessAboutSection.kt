package com.example.scrollbooker.ui.search.businessProfile.sections.about

import androidx.compose.foundation.layout.Box
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
import com.example.scrollbooker.components.customized.SectionMap
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessLocation
import com.example.scrollbooker.entity.booking.schedule.domain.model.Schedule
import com.example.scrollbooker.ui.theme.headlineSmall

@Composable
fun BusinessAboutSection(
    description: String,
    schedules: List<Schedule>,
    location: BusinessLocation,
    fullName: String
) {
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
            text = description
        )

        Spacer(Modifier.height(SpacingXL))

        Text(
            modifier = Modifier.padding(horizontal = BasePadding),
            text = stringResource(R.string.schedule),
            style = headlineSmall,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(Modifier.height(SpacingXL))

        SchedulesSection(
            modifier = Modifier.padding(horizontal = BasePadding),
            schedules = schedules
        )

        location.mapUrl?.let {
            SectionMap(
                modifier = Modifier.padding(horizontal = BasePadding),
                mapUrl = it,
                coordinates = location.coordinates,
                fullName = fullName
            )
        }

        Spacer(Modifier.height(SpacingXL))
    }
}