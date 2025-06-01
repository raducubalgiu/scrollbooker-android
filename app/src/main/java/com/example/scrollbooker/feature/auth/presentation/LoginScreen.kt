package com.example.scrollbooker.feature.auth.presentation

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
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.scrollbooker.R
import com.example.scrollbooker.components.inputs.Input
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.feature.auth.presentation.components.AuthScreen

@Composable
fun LoginScreen(
    authNavController: NavController,
    rootNavController: NavHostController,
    viewModel: AuthViewModel = hiltViewModel(),
) {
    val loginState by viewModel.loginState.collectAsState()

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    AuthScreen(
        navController = authNavController,
        type = stringResource(R.string.login),
        onSubmit = {
            viewModel.login(username, password)

//            if(loginState is FeatureState.Success) {
//                rootNavController.navigate(GlobalRoute.MAIN) {
//                    popUpTo(0) { inclusive = true }
//                }
//            }
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

        when(loginState) {
            is FeatureState.Loading -> {
                CircularProgressIndicator()
            }

            else -> {}
        }
    }
}