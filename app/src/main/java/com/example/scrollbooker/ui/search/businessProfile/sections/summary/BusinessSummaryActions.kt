package com.example.scrollbooker.ui.search.businessProfile.sections.summary

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ShapeDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButtonOutlined
import com.example.scrollbooker.components.core.divider.VerticalDivider
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessProfileCounters
import com.example.scrollbooker.ui.profile.components.userInfo.components.CounterItem
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.OnPrimary
import com.example.scrollbooker.ui.theme.Primary

@Composable
fun BusinessSummaryActions(
    counters: BusinessProfileCounters,
    hasPhone: Boolean,
    isFollow: Boolean,
    onFlyToReviewsSection: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            CounterItem(
                counter = counters.ratingsCount,
                label = stringResource(R.string.reviews),
                onNavigate = onFlyToReviewsSection
            )

            VerticalDivider()

            CounterItem(
                counter = counters.followersCount,
                label = stringResource(R.string.followers),
                onNavigate = {}
            )

            VerticalDivider()

            CounterItem(
                counter = counters.followingsCount,
                label = stringResource(R.string.following),
                onNavigate = {}
            )
        }

        Spacer(Modifier.height(SpacingXL))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if(hasPhone) {
                MainButtonOutlined(
                    modifier = Modifier.weight(0.5f),
                    title = stringResource(R.string.call),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Primary,
                        contentColor = OnPrimary,
                    ),
                    border = BorderStroke(1.dp, Primary),
                    shape = ShapeDefaults.ExtraLarge,
                    onClick = {}
                )

                Spacer(Modifier.width(SpacingS))
            }

            MainButtonOutlined(
                modifier = Modifier.weight(0.5f),
                title = stringResource(R.string.follow),
                shape = ShapeDefaults.ExtraLarge,
                colors = ButtonDefaults.buttonColors(
                    containerColor = when {
                        hasPhone -> Color.Transparent
                        isFollow -> Color.Transparent
                        else -> Primary
                    },
                    contentColor = when {
                        hasPhone -> OnBackground
                        isFollow -> OnBackground
                        else -> OnPrimary
                    }
                ),
                border = BorderStroke(1.dp, when {
                    hasPhone -> Divider
                    isFollow -> Divider
                    else -> Primary
                }),
                onClick = {}
            )
        }
    }
}