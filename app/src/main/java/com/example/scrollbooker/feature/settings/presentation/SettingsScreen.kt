package com.example.scrollbooker.feature.settings.presentation
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.Layout
import com.example.scrollbooker.components.core.list.ItemList
import com.example.scrollbooker.core.nav.routes.MainRoute
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.headlineLarge

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onBack: () -> Unit,
    onNavigate: (String) -> Unit
) {
    Layout(
        headerTitle = stringResource(R.string.settings),
        onBack = onBack,
    ) {
        Column(modifier = Modifier.padding(BasePadding)) {
            Text(
                style = headlineLarge,
                color = OnBackground,
                fontWeight = FontWeight.ExtraBold,
                text = stringResource(id = R.string.settings)
            )
        }

        Column(Modifier
            .fillMaxWidth()
            .background(SurfaceBG)
        ) {
            ItemList(
                headLine = stringResource(R.string.account),
                leftIcon = painterResource(R.drawable.ic_person),
                onClick = { onNavigate(MainRoute.Account.route) }
            )

            Spacer(Modifier.height(SpacingS))

            ItemList(
                headLine = stringResource(R.string.privacy),
                leftIcon = painterResource(R.drawable.ic_privacy),
                onClick = { onNavigate(MainRoute.Privacy.route) }
            )

            Spacer(Modifier.height(SpacingS))

            ItemList(
                headLine = stringResource(R.string.security),
                leftIcon = painterResource(R.drawable.ic_security),
                onClick = { onNavigate(MainRoute.Security.route) }
            )

            Spacer(Modifier.height(SpacingS))

            ItemList(
                headLine = stringResource(R.string.notifications),
                leftIcon = painterResource(R.drawable.ic_notifications),
                onClick = { onNavigate(MainRoute.NotificationSettings.route) }
            )

            Spacer(Modifier.height(SpacingS))

            ItemList(
                headLine = stringResource(R.string.display),
                leftIcon = painterResource(R.drawable.ic_theme),
                onClick = { onNavigate(MainRoute.Display.route) }
            )

            Spacer(Modifier.height(SpacingS))

            ItemList(
                headLine = stringResource(R.string.reportProblem),
                leftIcon = painterResource(R.drawable.ic_report),
                onClick = { onNavigate(MainRoute.ReportProblem.route) }
            )

            Spacer(Modifier.height(SpacingS))

            ItemList(
                headLine = stringResource(R.string.support),
                leftIcon = painterResource(R.drawable.ic_support),
                onClick = { onNavigate(MainRoute.Support.route) }
            )

            Spacer(Modifier.height(SpacingS))

            ItemList(
                headLine = stringResource(R.string.termsAndConditions),
                leftIcon = painterResource(R.drawable.ic_info),
                onClick = { onNavigate(MainRoute.TermsAndConditions.route) }
            )
        }
    }
}