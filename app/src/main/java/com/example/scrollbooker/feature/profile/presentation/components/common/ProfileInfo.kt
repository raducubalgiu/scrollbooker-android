package com.example.scrollbooker.feature.profile.presentation.components.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Repeat
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.feature.user.domain.model.User
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.Primary

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

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(
            top = SpacingXL,
            start = BasePadding,
            end = BasePadding
        ),
    ) {
        actions()
    }

    Column(modifier = Modifier
        .fillMaxSize().padding(BasePadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Repeat,
                contentDescription = null,
                tint = Primary
            )
            Spacer(Modifier.width(SpacingS))
            Text(
                text = "@house_of_barbers",
                color = OnBackground,
                fontWeight = FontWeight.SemiBold
            )
        }
        Spacer(Modifier.height(BasePadding))
        Text(
            text = user?.bio ?: "",
            textAlign = TextAlign.Center
        )
    }
}