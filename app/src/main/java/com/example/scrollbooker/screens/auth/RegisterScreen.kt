package com.example.scrollbooker.screens.auth

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.inputs.Input
import com.example.scrollbooker.core.util.Dimens.SpacingS

@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    onNavigate: (String) -> Unit
) {
    val registerState by viewModel.registerState.collectAsState()

    var email by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    AuthScreen(
        onNavigate = onNavigate,
        type = stringResource(R.string.register),
        onSubmit = {
            viewModel.register(
                email,
                username,
                password,
                roleName = "business",
                isValidated = false
            )
        }
    ) {
        Input(
            value = email,
            onValueChange = { email = it },
            label = "Email",
            placeholder = "Email"
        )

        Spacer(Modifier.height(SpacingS))

        Input(
            value = username,
            onValueChange = {username = it},
            label = "Username",
            placeholder = "Username"
        )

        Spacer(Modifier.height(SpacingS))

        Input(
            value = password,
            onValueChange = { password = it },
            label = "Password",
            placeholder = "Password"
        )
    }
}