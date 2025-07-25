package com.example.scrollbooker.ui.profile.components.userInformation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.divider.VerticalDivider
import com.example.scrollbooker.core.util.Dimens.SpacingXXL
import com.example.scrollbooker.entity.user.userProfile.domain.model.UserCounters
import com.example.scrollbooker.navigation.routes.MainRoute

@Composable
fun ProfileCounters(
    counters: UserCounters,
    isBusinessOrEmployee: Boolean,
    onNavigate: (String) -> Unit
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(
            vertical = SpacingXXL,
            horizontal = 70.dp
        ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        CounterItem(
            counter = counters.ratingsCount,
            label = if(!isBusinessOrEmployee) stringResource(R.string.bookings) else stringResource(R.string.reviews),
            onNavigate = { onNavigate("${MainRoute.UserSocial.route}/0") }
        )
        VerticalDivider()
        CounterItem(
            counter = counters.followersCount,
            label = stringResource(R.string.followers),
            onNavigate = { onNavigate("${MainRoute.UserSocial.route}/1") }
        )
        VerticalDivider()
        CounterItem(
            counter = counters.followingsCount,
            label = stringResource(R.string.following),
            onNavigate = { onNavigate("${MainRoute.UserSocial.route}/2") }
        )
    }
}