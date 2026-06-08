package com.example.scrollbooker.ui.profile.components.userInfo.components
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.OnPrimary
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun UserProfileActions(
    isBusinessOrEmployee: Boolean,
    isFollow: Boolean?,
    isFollowEnabled: Boolean,
    onFollow: (() -> Unit)? = null,
    onNavigateToBooking: () -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if(isBusinessOrEmployee) {
            ProfileActionButton(
                modifier = Modifier.weight(5f),
                containerColor = Primary,
                contentColor = OnPrimary,
                onClick = onNavigateToBooking
            ) {
                Text(
                    text = stringResource(R.string.book),
                    color = OnPrimary,
                    style = titleMedium,
                    fontWeight = FontWeight.Bold,
                )
            }
        }

        Spacer(Modifier.width(SpacingS))

        isFollow?.let {
            ProfileActionButton(
                modifier = Modifier
                    .weight(5f)
                    .border(
                        width = 1.dp,
                        color = if(isFollow) Divider else OnSurfaceBG,
                        shape = ShapeDefaults.ExtraLarge
                    ),
                containerColor = if(isFollow) Color.Transparent else SurfaceBG,
                contentColor = OnPrimary,
                isEnabled = isFollowEnabled,
                onClick = { onFollow?.invoke() }
            ) {
                Text(
                    text = if(isFollow) stringResource(R.string.following) else stringResource(R.string.follow),
                    color = if(isFollow) OnBackground else OnSurfaceBG,
                    style = titleMedium,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}