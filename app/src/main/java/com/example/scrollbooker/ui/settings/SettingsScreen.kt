package com.example.scrollbooker.ui.settings
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
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
import com.example.scrollbooker.navigation.navigators.SettingsNavigator
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.headlineLarge

data class SettingsItem(
    val icon: Int,
    val headline: String,
    val navigate: () -> Unit
)

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    settingsNavigate: SettingsNavigator,
    onLogout: () -> Unit
) {
    val editProfileItems = listOf(
        SettingsItem(
            headline = stringResource(R.string.account),
            icon = R.drawable.ic_person_outline,
            navigate = { settingsNavigate.toAccount() }
        ),
        SettingsItem(
            headline = stringResource(R.string.privacy),
            icon = R.drawable.ic_lock_closed_outline,
            navigate = { settingsNavigate.toPrivacy() }
        ),
        SettingsItem(
            headline = stringResource(R.string.security),
            icon = R.drawable.ic_shield_check_outline,
            navigate = { settingsNavigate.toSecurity() }
        ),
        SettingsItem(
            headline = stringResource(R.string.display),
            icon = R.drawable.ic_moon_outline,
            navigate = { settingsNavigate.toDisplay() }
        ),
        SettingsItem(
            headline = stringResource(R.string.notifications),
            icon = R.drawable.ic_notifications_outline,
            navigate = { settingsNavigate.toNotifications() }
        ),
        SettingsItem(
            headline = stringResource(R.string.reportProblem),
            icon = R.drawable.ic_flag_outline,
            navigate = { settingsNavigate.toReportProblem() }
        ),
        SettingsItem(
            headline = stringResource(R.string.support),
            icon = R.drawable.ic_comment_outline,
            navigate = { settingsNavigate.toSupport() }
        ),
        SettingsItem(
            headline = stringResource(R.string.termsAndConditions),
            icon = R.drawable.ic_info_outline,
            navigate = { settingsNavigate.toTermsAndConditions() }
        )
    )

    Layout(
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
                    onClick = item.navigate
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