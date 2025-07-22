package com.example.scrollbooker.ui.profile.myProfile.settings
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.Layout
import com.example.scrollbooker.components.core.list.ItemList
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.util.Dimens.SpacingXS
import com.example.scrollbooker.navigation.routes.MainRoute
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.headlineLarge

data class EditProfileItem(
    val icon: Int,
    val headline: String,
    val navigate: String
)

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onNavigate: (String) -> Unit,
    onLogout: () -> Unit
) {
    val editProfileItems = listOf(
        EditProfileItem(
            headline = stringResource(R.string.account),
            icon = R.drawable.ic_person_outline,
            navigate = MainRoute.Account.route
        ),
        EditProfileItem(
            headline = stringResource(R.string.privacy),
            icon = R.drawable.ic_lock_closed_outline,
            navigate = MainRoute.Privacy.route
        ),
        EditProfileItem(
            headline = stringResource(R.string.security),
            icon = R.drawable.ic_shield_check_outline,
            navigate = MainRoute.Security.route
        ),
        EditProfileItem(
            headline = stringResource(R.string.display),
            icon = R.drawable.ic_moon_outline,
            navigate = MainRoute.Display.route
        ),
        EditProfileItem(
            headline = stringResource(R.string.notifications),
            icon = R.drawable.ic_notifications_outline,
            navigate = MainRoute.NotificationSettings.route
        ),
        EditProfileItem(
            headline = stringResource(R.string.reportProblem),
            icon = R.drawable.ic_flag_outline,
            navigate = MainRoute.ReportProblem.route
        ),
        EditProfileItem(
            headline = stringResource(R.string.support),
            icon = R.drawable.ic_comment_outline,
            navigate = MainRoute.Support.route
        ),
        EditProfileItem(
            headline = stringResource(R.string.termsAndConditions),
            icon = R.drawable.ic_info_outline,
            navigate = MainRoute.TermsAndConditions.route
        )
    )

    Layout(
        modifier = Modifier.statusBarsPadding(),
        headerTitle = "",
        onBack = onBack,
        enablePaddingH = false
    ) {
        Column(modifier = Modifier.padding(BasePadding)) {
            Text(
                style = headlineLarge,
                color = OnBackground,
                fontWeight = FontWeight.ExtraBold,
                text = stringResource(id = R.string.settings)
            )
        }

        LazyColumn {
            itemsIndexed(editProfileItems) { index, item ->
                ItemList(
                    headLine = item.headline,
                    leftIcon = painterResource(item.icon),
                    onClick = { onNavigate(item.navigate) }
                )

                if(index < editProfileItems.size) {
                    HorizontalDivider(
                        modifier = Modifier.padding(
                            horizontal = SpacingXL,
                            vertical = SpacingXS,
                        ),
                        color = Divider,
                        thickness = 0.55.dp
                    )
                }
            }

            item {
                ItemList(
                    headLine = stringResource(R.string.logout),
                    leftIcon = painterResource(R.drawable.ic_logout),
                    onClick = onLogout,
                    displayRightIcon = false,
                    color = Error
                )
            }
        }
    }
}