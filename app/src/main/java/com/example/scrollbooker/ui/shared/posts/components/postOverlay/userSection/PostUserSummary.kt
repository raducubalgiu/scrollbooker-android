package com.example.scrollbooker.ui.shared.posts.components.postOverlay.userSection

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.ui.theme.Divider

@Composable
fun PostUserSummary(
    profession: String,
    fullName: String,
    avatar: String,
    distance: Float?,
    onNavigateToUser: () -> Unit
) {
    Column {
        Spacer(Modifier.height(SpacingS))

        Row(verticalAlignment = Alignment.CenterVertically) {
            PostBusinessOrEmployeeLabel(
                fullName = fullName,
                avatar = avatar,
                onNavigateToUser = onNavigateToUser
            )
            Spacer(Modifier.width(SpacingS))

            Box(Modifier
                .size(4.dp)
                .clip(CircleShape)
                .background(Divider)
            )

            Spacer(Modifier.width(SpacingS))

            distance?.let { PostDistanceLabel(it) }
        }
        Spacer(Modifier.height(SpacingS))
    }
}