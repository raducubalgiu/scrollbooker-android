package com.example.scrollbooker.screens.auth

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.inputs.Input
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.FeatureState

@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    onNavigate: (String) -> Unit
) {
    val registerState by viewModel.registerState.collectAsState()
    val isLoading = registerState is FeatureState.Loading
    val isRegisterSuccess = registerState is FeatureState.Success<Unit>

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    AuthScreen(
        isEnabled = isLoading,
        onNavigate = onNavigate,
        type = stringResource(R.string.register),
        onSubmit = {
            viewModel.register(
                email,
                password
            )
        }
    ) {
        Input(
            value = email,
            onValueChange = { email = it },
            label = stringResource(R.string.email),
            placeholder = stringResource(R.string.email),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
        )

        Spacer(Modifier.height(SpacingS))

        Input(
            value = password,
            onValueChange = { password = it },
            label = stringResource(R.string.password),
            placeholder = stringResource(R.string.password),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            )
        )
    }
}