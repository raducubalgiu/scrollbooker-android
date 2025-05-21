package com.example.scrollbooker.feature.auth.presentation.collectClientDetails

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.scrollbooker.R
import com.example.scrollbooker.components.Input
import com.example.scrollbooker.core.nav.routes.AuthRoute
import com.example.scrollbooker.core.util.Dimens.BasePadding

@Composable
fun CollectUsernameScreen(navController: NavController) {
    CollectClientDetails(
        headLine = stringResource(id = R.string.username),
        subHeadLine = stringResource(id = R.string.addUniqueUsername),
        screenSize = 3,
        selectedScreen = 0,
        onOmit = { navController.navigate(AuthRoute.BirthDate.route) },
        onNext = { navController.navigate(AuthRoute.BirthDate.route) },
        content = {
            var username by remember { mutableStateOf("") }

            Input(
                value = username,
                onValueChange = { username = it },
                label = "Username"
            )

            Spacer(modifier = Modifier.height(BasePadding))

            Text(
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                text = "Numele de utilizator poate contine doar litere mici, numbere, underscore, si perioade."
            )

            Spacer(modifier = Modifier.height(BasePadding))

            Text(
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                text = "Iti poti schimba numele de utilizator o data la 30 de zile"
            )
        }
    )
}