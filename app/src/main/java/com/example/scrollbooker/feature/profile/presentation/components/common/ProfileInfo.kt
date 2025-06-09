package com.example.scrollbooker.feature.profile.presentation.components.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.feature.profile.presentation.components.ProfileCounters
import com.example.scrollbooker.feature.profile.presentation.components.ProfileUserInfo
import com.example.scrollbooker.feature.user.domain.model.User

@Composable
fun ProfileInfo(
    user: User?,
    onNavigateCounters: (String) -> Unit,
    onShowSchedule: () -> Unit,
    actions: @Composable () -> Unit
) {
    ProfileCounters(
        counters=user?.counters,
        onNavigate = onNavigateCounters
    )

    ProfileUserInfo(
        fullName = user?.fullName,
        profession = user?.profession,
        onOpenScheduleSheet = onShowSchedule
    )

    actions()

    Column(modifier = Modifier
        .fillMaxSize().padding(BasePadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = user?.bio ?: "",
            textAlign = TextAlign.Center
        )
    }
}