package com.example.scrollbooker.ui.profile.components.profileHeader

import androidx.compose.runtime.Composable
import com.example.scrollbooker.entity.user.userProfile.domain.model.UserProfile
import com.example.scrollbooker.navigation.navigators.NavigateSocialParam
import com.example.scrollbooker.ui.profile.components.myProfile.MyProfileActions
import com.example.scrollbooker.ui.profile.components.profileHeader.components.ProfileCounters

@Composable
fun ProfileHeader(
    user: UserProfile,
    onOpenScheduleSheet: () -> Unit,
    onNavigateToSocial: (NavigateSocialParam) -> Unit,
    onNavigateToBusinessOwner: (Int?) -> Unit,
    actions: @Composable () -> Unit
) {
    ProfileCounters(
        counters = user.counters,
        isBusinessOrEmployee = user.isBusinessOrEmployee,
        onNavigateToSocial = { tabIndex ->
            onNavigateToSocial(
                NavigateSocialParam(
                    tabIndex = tabIndex,
                    userId = user.id,
                    username = user.username,
                    isBusinessOrEmployee = user.isBusinessOrEmployee
                )
            )
        }
    )

    ProfileUserInfo(
        user = user,
        actions = {
            actions()
        },
        onOpenScheduleSheet = onOpenScheduleSheet,
        onNavigateToBusinessOwner = onNavigateToBusinessOwner
    )
}