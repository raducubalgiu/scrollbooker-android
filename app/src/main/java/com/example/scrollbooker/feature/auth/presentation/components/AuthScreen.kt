package com.example.scrollbooker.feature.auth.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.MainButton
import com.example.scrollbooker.core.nav.routes.AuthRoute
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.headlineLarge

@Composable
fun AuthScreen(
    navController: NavController,
    type: String,
    onSubmit: () -> Unit,
    content: @Composable () -> Unit,
) {
    val isLogin = type == stringResource(R.string.login)
    val footerText =
        if(isLogin) stringResource(R.string.dontHaveAnAccount)
        else stringResource(R.string.alreadyHaveAnAccount)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(
                top = 50.dp,
                bottom = BasePadding,
                start = BasePadding,
                end = BasePadding
            ),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                style = headlineLarge,
                color = OnBackground,
                fontWeight = FontWeight.ExtraBold,
                text = type
            )
            Text(
                text =
                    if(isLogin) stringResource(R.string.loginMessage)
                    else stringResource(R.string.registerMessage)
            )
            Spacer(Modifier.height(BasePadding))

            content()

            Spacer(Modifier.height(BasePadding))

            MainButton(
                title = stringResource(id = R.string.login),
                onClick = onSubmit
            )
        }

        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    color = OnBackground,
                    text = "${footerText}?"
                )
                TextButton(onClick = {
                    navController.navigate(route =
                        if(isLogin) AuthRoute.Register.route
                        else AuthRoute.Login.route)
                }) {
                    Text(
                        style = bodyLarge,
                        fontWeight = FontWeight.Bold,
                        text = stringResource(id =
                            if(isLogin) R.string.register
                            else R.string.login
                        )
                    )
                }
            }
        }
    }
}