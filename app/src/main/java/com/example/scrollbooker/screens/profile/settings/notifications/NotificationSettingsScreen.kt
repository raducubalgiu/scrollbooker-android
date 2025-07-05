package com.example.scrollbooker.screens.profile.settings.notifications
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.Layout
import com.example.scrollbooker.components.core.list.ItemSwitch
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXXS
import com.example.scrollbooker.ui.theme.Divider

@Composable
fun NotificationSettings(
    onBack: () -> Unit
) {
    Layout(
        modifier = Modifier.statusBarsPadding(),
        headerTitle = stringResource(R.string.notifications),
        onBack = onBack,
        enablePaddingH = false
    ) {
        Column(Modifier.padding(vertical = BasePadding)) {
            ItemSwitch(
                headLine = "Toate",
                supportingText = "Alege sa opresti toate notificarile o perioada",
                onClick = {},
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = SpacingXXS),
                color = Divider
            )

            ItemSwitch(
                headLine = "Aprecieri",
                onClick = {},
            )

            ItemSwitch(
                headLine = "Comentarii",
                onClick = {},
            )

            ItemSwitch(
                headLine = "Urmaritori noi",
                onClick = {},
            )

            ItemSwitch(
                headLine = "Mentions and Tags",
                onClick = {},
            )
        }
    }
}