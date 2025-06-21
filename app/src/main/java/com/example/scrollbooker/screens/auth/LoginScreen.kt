package com.example.scrollbooker.screens.auth

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.inputs.Input
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.FeatureState

@Composable
fun LoginScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onNavigate: (String) -> Unit
) {
    val authState by viewModel.authState.collectAsState()

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    AuthScreen(
        onNavigate = onNavigate,
        type = stringResource(R.string.login),
        onSubmit = {
            viewModel.login(username, password)
        }
    ) {
        Input(
            value = username,
            onValueChange = { username = it },
            label = "Username",
            placeholder = "Username"
        )

        Spacer(Modifier.height(SpacingS))

        Input(
            value = password,
            onValueChange = { password = it },
            label = "Password",
            placeholder = "Username"
        )

        when(authState) {
            is FeatureState.Loading -> {
                CircularProgressIndicator()
            }

            else -> {}
        }
    }
}