package com.example.scrollbooker.feature.profile.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXXL

@Composable
fun ProfileLayout(
    ratingsCount: Int,
    followersCount: Int,
    followingsCount: Int,
    fullName: String,
    profession: String,
    onNavigateCounters: (String) -> Unit,
    onOpenScheduleSheet: () -> Unit,
    header: @Composable () -> Unit,
    actions: @Composable () -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        header()
        ProfileCounters(
            ratingsCount,
            followersCount,
            followingsCount,
            onNavigate = { onNavigateCounters(it) }
        )
        Spacer(Modifier.height(BasePadding))
        ProfileUserInfo(
            fullName = fullName,
            profession = profession,
            onOpenScheduleSheet = onOpenScheduleSheet
        )
        Column(modifier = Modifier
            .padding(
                vertical = SpacingXXL,
                horizontal = BasePadding)
        ) { actions() }
        ProfileTabs()
    }
}