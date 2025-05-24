package com.example.scrollbooker.feature.settings.presentation

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
import com.example.scrollbooker.core.util.Dimens.BasePadding

@Composable
fun SettingsScreen(navController: NavController) {
    Column(modifier = Modifier.fillMaxSize()) {
        Header(title = "", navController = navController)

        Column(modifier = Modifier.padding(BasePadding)) {
            Text(
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.ExtraBold,
                text = stringResource(id = R.string.settings)
            )
        }

        ItemList(
            headLine = "Account",
            leftIcon = painterResource(R.drawable.ic_person),
            onClick = {}
        )

        ItemList(
            headLine = "Privacy",
            leftIcon = painterResource(R.drawable.ic_privacy),
            onClick = {}
        )

        ItemList(
            headLine = "Security",
            leftIcon = painterResource(R.drawable.ic_security),
            onClick = {}
        )

        ItemList(
            headLine = "Notifications",
            leftIcon = painterResource(R.drawable.ic_notifications),
            onClick = {}
        )

        ItemList(
            headLine = "Display",
            leftIcon = painterResource(R.drawable.ic_theme),
            onClick = {}
        )

        ItemList(
            headLine = "Report a Problem",
            leftIcon = painterResource(R.drawable.ic_report),
            onClick = {}
        )

        ItemList(
            headLine = "Support",
            leftIcon = painterResource(R.drawable.ic_support),
            onClick = {}
        )

        ItemList(
            headLine = "Terms and Policies",
            leftIcon = painterResource(R.drawable.ic_info),
            onClick = {}
        )
    }
}