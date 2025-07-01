package com.example.scrollbooker.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.components.core.headers.Header
import com.example.scrollbooker.components.core.inputs.Input
import com.example.scrollbooker.core.nav.host.AuthTypeEnum
import com.example.scrollbooker.core.nav.routes.AuthRoute
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.checkEmail
import com.example.scrollbooker.core.util.checkPassword
import com.example.scrollbooker.screens.auth.components.AuthFooter
import com.example.scrollbooker.screens.auth.components.AuthHeader
import com.example.scrollbooker.screens.auth.components.PasswordRequirement
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.bodyMedium
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun AuthScreen(
    viewModel: AuthViewModel,
    type: AuthTypeEnum,
    onBack: (() -> Unit)? = null,
    onNavigate: (String) -> Unit,
    onSubmit: (email: String, username: String, password: String) -> Unit,
) {
    val authState by viewModel.authState.collectAsState()
    val isLoading = authState is FeatureState.Loading

    var email by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var wasSubmitted by remember { mutableStateOf(false) }

    val emailError = if(wasSubmitted) checkEmail(email) else null
    val isValidPassword = checkPassword(password)

    val isValid = when(type) {
        AuthTypeEnum.LOGIN -> wasSubmitted && isValidPassword
        else -> wasSubmitted && emailError == null
    }

    val isEnabled = when(type) {
        AuthTypeEnum.LOGIN -> username.isNotEmpty() && password.isNotEmpty()
        else -> email.isNotEmpty() && password.isNotEmpty()
    }

    Column(Modifier.fillMaxSize().background(Background)) {
        if(type == AuthTypeEnum.REGISTER_BUSINESS) {
            Header(
                onBack = onBack
            )
        }

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
                AuthHeader(type)

                if(type == AuthTypeEnum.LOGIN) {
                    Input(
                        value = username,
                        onValueChange = { username = it },
                        label = stringResource(R.string.username),
                        placeholder = stringResource(R.string.username),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        )
                    )
                } else {
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
                        isError = emailError != null && type == AuthTypeEnum.REGISTER
                    )
                }

                if(emailError != null && type == AuthTypeEnum.REGISTER) {
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

                Spacer(Modifier.height(SpacingS))

                if(wasSubmitted && !isValidPassword) {
                    PasswordRequirement(password)
                }

                Spacer(Modifier.height(BasePadding))

                MainButton(
                    isLoading = isLoading,
                    enabled = !isLoading && isEnabled,
                    title = if(type == AuthTypeEnum.LOGIN) stringResource(id = R.string.login)
                    else stringResource(R.string.register),
                    onClick = {
                        wasSubmitted = true
                        if(isValid) {
                            onSubmit(email, username, password)
                        }
                    }
                )

                Spacer(Modifier.height(BasePadding))

                if(type != AuthTypeEnum.REGISTER_BUSINESS) {
                    AuthFooter(type, onNavigate = {
                        onNavigate(
                            if(type == AuthTypeEnum.LOGIN) AuthRoute.RegisterClient.route
                            else AuthRoute.Login.route
                        )
                    })
                }
            }

            if(type != AuthTypeEnum.REGISTER_BUSINESS) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    HorizontalDivider(color = Divider, thickness = 0.5.dp)

                    Spacer(Modifier.height(BasePadding))

                    Text(
                        text = "Ai un business care primeste programari?",
                        style = titleMedium,
                        fontWeight = FontWeight.Normal
                    )

                    Spacer(Modifier.height(BasePadding))

                    Button(
                        onClick = { onNavigate(AuthRoute.RegisterBusiness.route) }
                    ) {
                        Text(
                            style = bodyLarge,
                            fontWeight = FontWeight.Bold,
                            text = "Inregistreaza"
                        )
                    }
                }
            }
        }
    }
}