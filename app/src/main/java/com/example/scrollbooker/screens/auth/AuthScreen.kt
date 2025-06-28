package com.example.scrollbooker.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.components.core.inputs.Input
import com.example.scrollbooker.core.nav.host.AuthTypeEnum
import com.example.scrollbooker.core.nav.routes.AuthRoute
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.checkEmail
import com.example.scrollbooker.core.util.checkPassword
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.bodyMedium
import com.example.scrollbooker.ui.theme.headlineLarge

@Composable
fun AuthScreen(
    viewModel: AuthViewModel,
    type: AuthTypeEnum,
    onNavigate: (String) -> Unit,
    onSubmit: (email: String, password: String) -> Unit,
) {
    val authState by viewModel.authState.collectAsState()
    val isLoading = authState is FeatureState.Loading

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var wasSubmitted by remember { mutableStateOf(false) }
    val emailError = if(wasSubmitted) checkEmail(email) else null
    val isValidPassword = checkPassword(password)

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
                text = if(type == AuthTypeEnum.LOGIN) stringResource(R.string.login)
                    else stringResource(R.string.register)
            )
            Text(
                text =
                    if(type == AuthTypeEnum.LOGIN) stringResource(R.string.loginMessage)
                    else stringResource(R.string.registerMessage)
            )
            Spacer(Modifier.height(BasePadding))

            Input(
                value = email,
                onValueChange = {
                    email = it
                    if(wasSubmitted) wasSubmitted = false
                },
                label = stringResource(R.string.email),
                placeholder = stringResource(R.string.email),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                isError = emailError != null
            )

            if(emailError != null) {
                Column(modifier = Modifier
                    .padding(vertical = BasePadding)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.Warning,
                            contentDescription = null,
                            tint = Error
                        )
                        Spacer(Modifier.width(SpacingS))
                        Text(
                            text = emailError,
                            color = Error,
                            style = bodyMedium
                        )
                    }
                }
            }

            Spacer(Modifier.height(SpacingS))

            Input(
                value = password,
                onValueChange = { password = it },
                label = stringResource(R.string.password),
                placeholder = stringResource(R.string.password),
                isError = wasSubmitted && !isValidPassword,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                )
            )

            if(wasSubmitted && !isValidPassword) {
                Spacer(Modifier.height(SpacingS))

                Column {
                    RequirementItem(
                        stringResource(R.string.passwordShouldContainMinimumEightCharacters),
                        password.length > 8
                    )
                    RequirementItem(
                        stringResource(R.string.passwordShouldContainMaximumTwentyCharacters),
                        password.length < 20
                    )
                    RequirementItem(
                        stringResource(R.string.passwordShouldContainAtLeastOneBigLetter),
                        password.any { it.isUpperCase() }
                    )
                    RequirementItem(
                        stringResource(R.string.passwordShouldContainAtLeastOneDigit),
                        password.any { it.isDigit() }
                    )
                }
            }

            Spacer(Modifier.height(BasePadding))

            MainButton(
                isLoading = isLoading,
                enabled = emailError == null && !isLoading,
                title = stringResource(id = R.string.login),
                onClick = {
                    wasSubmitted = true
                    if(checkEmail(email) == null) {
                        onSubmit(email, password)
                    }
                }
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
                    text = if(type == AuthTypeEnum.LOGIN) stringResource(R.string.dontHaveAnAccount)
                    else stringResource(R.string.alreadyHaveAnAccount)
                )
                TextButton(
                    onClick = {
                        onNavigate(
                            if(type == AuthTypeEnum.LOGIN) AuthRoute.RegisterClient.route
                            else AuthRoute.Login.route
                        )
                    }
                ) {
                    Text(
                        style = bodyLarge,
                        fontWeight = FontWeight.Bold,
                        text = stringResource(id =
                            if(type == AuthTypeEnum.LOGIN) R.string.register
                            else R.string.login
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun RequirementItem(text: String, fulfilled: Boolean) {
    val icon = if(fulfilled) Icons.Default.Check else Icons.Default.Close
    val color = if(fulfilled) Color.Green else Error

    Row(
        modifier = Modifier.padding(top = SpacingS),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            tint = color,
            contentDescription = null
        )
        Spacer(Modifier.width(SpacingS))
        Text(text = text)
    }
}