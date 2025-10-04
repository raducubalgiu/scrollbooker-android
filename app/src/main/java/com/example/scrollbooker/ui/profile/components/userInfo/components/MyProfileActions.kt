package com.example.scrollbooker.ui.profile.components.userInfo.components
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun MyProfileActions(
    onEditProfile: () -> Unit,
    isBusinessOrEmployee: Boolean,
    onNavigateToMyCalendar: () -> Unit
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

        if(isBusinessOrEmployee) {
            ProfileActionButton(
                modifier = Modifier.weight(0.5f),
                onClick = onNavigateToMyCalendar
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.ic_calendar_outline),
                        contentDescription = null
                    )
                    Spacer(Modifier.width(SpacingS))
                    Text(
                        text = "Calendar",
                        style = titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = OnBackground
                    )
                }
            }
        } else {
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
}