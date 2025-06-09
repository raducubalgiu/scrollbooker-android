package com.example.scrollbooker.feature.profile.presentation.components.myProfile

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.feature.profile.presentation.components.common.ProfileActionButton

@Composable
fun MyProfileActions(
    onEditProfile: () -> Unit
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(
            top = SpacingXL,
            start = BasePadding,
            end = BasePadding
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProfileActionButton(
            modifier = Modifier.weight(5f),
            onClick = onEditProfile
        ) {
            Text(
                text = stringResource(R.string.editProfile)
            )
        }
        Spacer(Modifier.width(SpacingS))
        ProfileActionButton(
            modifier = Modifier.weight(5f),
            onClick = {}
        ) {
            Text(
                text = stringResource(R.string.shareProfile)
            )
        }
    }
}