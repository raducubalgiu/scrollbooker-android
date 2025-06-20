package com.example.scrollbooker.screens.auth

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.inputs.Input
import com.example.scrollbooker.core.util.Dimens.SpacingS

@Composable
fun RegisterScreen(
    navController: NavController,
    //viewModel: AuthViewModel = hiltViewModel(),
) {
    //val state by viewModel.registerState.collectAsState()

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    AuthScreen(
        navController = navController,
        type = stringResource(R.string.register),
        onSubmit = {}
    ) {
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