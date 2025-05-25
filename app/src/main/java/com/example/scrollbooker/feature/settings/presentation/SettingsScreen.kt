package com.example.scrollbooker.feature.settings.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.example.scrollbooker.R
import com.example.scrollbooker.components.Header
import com.example.scrollbooker.components.ItemList
import com.example.scrollbooker.core.nav.routes.MainRoute
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.OnBackground

@Composable
fun SettingsScreen(navController: NavController) {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(Background)) {
        Header(title = "", navController = navController)

        Column(modifier = Modifier.padding(BasePadding)) {
            Text(
                style = MaterialTheme.typography.headlineLarge,
                color = OnBackground,
                fontWeight = FontWeight.ExtraBold,
                text = stringResource(id = R.string.settings)
            )
        }

        ItemList(
            headLine = stringResource(R.string.account),
            leftIcon = painterResource(R.drawable.ic_person),
            onClick = { navController.navigate(MainRoute.Account.route) }
        )

        ItemList(
            headLine = stringResource(R.string.privacy),
            leftIcon = painterResource(R.drawable.ic_privacy),
            onClick = { navController.navigate(MainRoute.Privacy.route) }
        )

        ItemList(
            headLine = stringResource(R.string.security),
            leftIcon = painterResource(R.drawable.ic_security),
            onClick = { navController.navigate(MainRoute.Security.route) }
        )

        ItemList(
            headLine = stringResource(R.string.notifications),
            leftIcon = painterResource(R.drawable.ic_notifications),
            onClick = { navController.navigate(MainRoute.NotificationSettings.route) }
        )

        ItemList(
            headLine = stringResource(R.string.display),
            leftIcon = painterResource(R.drawable.ic_theme),
            onClick = { navController.navigate(MainRoute.Display.route) }
        )

        ItemList(
            headLine = stringResource(R.string.reportProblem),
            leftIcon = painterResource(R.drawable.ic_report),
            onClick = { navController.navigate(MainRoute.ReportProblem.route) }
        )

        ItemList(
            headLine = stringResource(R.string.support),
            leftIcon = painterResource(R.drawable.ic_support),
            onClick = { navController.navigate(MainRoute.Support.route) }
        )

        ItemList(
            headLine = stringResource(R.string.termsAndConditions),
            leftIcon = painterResource(R.drawable.ic_info),
            onClick = { navController.navigate(MainRoute.TermsAndConditions.route) }
        )
    }
}