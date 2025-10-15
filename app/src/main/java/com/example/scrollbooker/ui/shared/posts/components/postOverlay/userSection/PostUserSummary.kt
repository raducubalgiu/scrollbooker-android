package com.example.scrollbooker.ui.shared.posts.components.postOverlay.userSection

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.scrollbooker.core.util.Dimens.SpacingS

@Composable
fun PostUserSummary(
    profession: String,
    fullName: String,
    avatar: String,
    distance: Float?,
    onNavigateToUser: () -> Unit
) {
    PostProfessionLabel(profession = profession)
    Spacer(Modifier.height(SpacingS))

    Row(verticalAlignment = Alignment.CenterVertically) {
        PostBusinessOrEmployeeLabel(
            fullName = fullName,
            avatar = avatar,
            onNavigateToUser = onNavigateToUser
        )
        Spacer(Modifier.width(SpacingS))

        distance?.let { PostDistanceLabel(it) }
    }
    Spacer(Modifier.height(SpacingS))
}