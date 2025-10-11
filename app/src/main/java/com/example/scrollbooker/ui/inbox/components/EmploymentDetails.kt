package com.example.scrollbooker.ui.inbox.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.avatar.AvatarWithBadge
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.entity.user.userSocial.domain.model.UserSocial
import com.example.scrollbooker.ui.theme.bodyLarge

@Composable
fun EmploymentDetails(
    employer: UserSocial
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AvatarWithBadge(
            url = employer.avatar ?: "",
            badgeIconImageVector = Icons.Default.Repeat,
        )

        Spacer(Modifier.height(SpacingXL))

        Text(
            style = bodyLarge,
            fontWeight = FontWeight.ExtraBold,
            text = "${employer.fullName} ${stringResource(R.string.sentYouAnEmploymentRequest)}"
        )
    }
}