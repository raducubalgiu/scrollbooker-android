package com.example.scrollbooker.feature.profile.presentation.components

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
import com.example.scrollbooker.components.core.VerticalDivider
import com.example.scrollbooker.core.nav.routes.MainRoute
import com.example.scrollbooker.core.util.Dimens.SpacingXXL

@Composable
fun ProfileCounters(
    ratingsCount: Int,
    followersCount: Int,
    followingsCount: Int,
    onNavigate: (String) -> Unit

) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 70.dp, vertical = SpacingXXL),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        CounterItem(
            counter = ratingsCount,
            label = stringResource(R.string.reviews),
            onNavigate = { onNavigate("${MainRoute.UserSocial.route}/0") }
        )
        VerticalDivider()
        CounterItem(
            counter = followersCount,
            label = stringResource(R.string.followers),
            onNavigate = { onNavigate("${MainRoute.UserSocial.route}/1") }
        )
        VerticalDivider()
        CounterItem(
            counter = followingsCount,
            label = stringResource(R.string.following),
            onNavigate = { onNavigate("${MainRoute.UserSocial.route}/2") }
        )
    }
}