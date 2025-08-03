package com.example.scrollbooker.ui.profile.components.profileHeader

import androidx.compose.runtime.Composable
import com.example.scrollbooker.entity.user.userProfile.domain.model.UserProfile
import com.example.scrollbooker.ui.profile.NavigateSocialParam
import com.example.scrollbooker.ui.profile.components.myProfile.MyProfileActions
import com.example.scrollbooker.ui.profile.components.profileHeader.components.ProfileCounters

@Composable
fun ProfileHeader(
    user: UserProfile,
    onOpenScheduleSheet: () -> Unit,
    onNavigateToEditProfile: (() -> Unit)? = null,
    onNavigateToSocial: (NavigateSocialParam) -> Unit,
    onNavigateToBusinessOwner: (Int?) -> Unit
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
            MyProfileActions(
                onEditProfile = { onNavigateToEditProfile?.invoke() },
                isBusinessOrEmployee = user.isBusinessOrEmployee
            )
        },
        onOpenScheduleSheet = onOpenScheduleSheet,
        onNavigateToBusinessOwner = onNavigateToBusinessOwner
    )
}