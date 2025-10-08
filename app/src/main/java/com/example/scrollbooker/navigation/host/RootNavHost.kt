package com.example.scrollbooker.navigation.host
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.ui.auth.AuthViewModel

@Composable
fun RootNavHost(
    navController: NavHostController,
    viewModel: AuthViewModel
) {
    val authState by viewModel.authState.collectAsState()

    when(val state = authState) {
        is FeatureState.Success -> {
            val registrationStep = state.data.registrationStep?.key

            if(state.data.isValidated) {
                MainNavHost(
                    navController = navController,
                    authViewModel = viewModel
                )
            } else if(registrationStep != null) {
                OnboardingNavHost(
                    navController = navController,
                    authViewModel = viewModel,
                    startDestination = registrationStep
                )
            } else {
                AuthNavHost(viewModel)
            }
        }
        else -> AuthNavHost(viewModel)
    }
}