package com.example.scrollbooker.screens.profile.components.myProfile
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.screens.profile.components.common.ProfileActionButton
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun MyProfileActions(
    onEditProfile: () -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProfileActionButton(
            modifier = Modifier.weight(0.5f),
            onClick = onEditProfile
        ) {
            Text(
                text = stringResource(R.string.editProfile),
                style = titleMedium,
                fontWeight = FontWeight.Bold,
                color = OnBackground
            )
        }
        Spacer(Modifier.width(SpacingS))
        ProfileActionButton(
            modifier = Modifier.weight(0.5f),
            onClick = {}
        ) {
            Text(
                text = stringResource(R.string.shareProfile),
                style = titleMedium,
                fontWeight = FontWeight.Bold,
                color = OnBackground
            )
        }
    }
}