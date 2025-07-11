package com.example.scrollbooker.screens.profile.components.userProfile
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.screens.profile.components.userInformation.components.ProfileActionButton
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.OnPrimary
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun UserProfileActions(
    isFollow: Boolean,
    onNavigateToCalendar: () -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProfileActionButton(
            modifier = Modifier.weight(5f),
            containerColor = if(isFollow) SurfaceBG else Primary,
            contentColor = OnPrimary,
            onClick = { }
        ) {
            Text(
                text = if(isFollow) stringResource(R.string.following) else stringResource(R.string.follow),
                color = if(isFollow) OnBackground else OnPrimary,
                style = titleMedium,
                fontWeight = FontWeight.Bold,
            )
        }
        Spacer(Modifier.width(SpacingS))
        ProfileActionButton(
            modifier = Modifier.weight(5f),
            onClick = onNavigateToCalendar
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
        Spacer(Modifier.width(SpacingS))
        ProfileActionButton(
            modifier = Modifier.weight(1.5f),
            onClick = {}
        ) {
            Icon(
                modifier = Modifier.size(20.dp),
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null
            )
        }
    }
}