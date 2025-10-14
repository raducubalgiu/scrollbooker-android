package com.example.scrollbooker.navigation.host
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.navigation.routes.AuthRoute
import com.example.scrollbooker.navigation.routes.MainRoute
import com.example.scrollbooker.ui.auth.AuthViewModel
import kotlinx.coroutines.launch

@Composable
fun RootNavHost(
    navController: NavHostController,
    viewModel: AuthViewModel
) {
    val authState by viewModel.authState.collectAsState()
    val scope = rememberCoroutineScope()

    when(val state = authState) {
        is FeatureState.Success -> {
            val registrationStep = state.data.registrationStep?.key

            if(state.data.isValidated) {
                MainNavHost(
                    navController = navController,
                    onLogout = {
                        scope.launch {
                            navController.navigate(AuthRoute.Login.route) {
                                popUpTo(navController.graph.id) { inclusive = true }
                                launchSingleTop = true
                            }

                            viewModel.logout()
                        }
                    }
                )
            } else if(registrationStep != null) {
                OnboardingNavHost(
                    navController = navController,
                    authViewModel = viewModel,
                    startDestination = registrationStep
                )
            } else {
                AuthNavHost(
                    authViewModel = viewModel,
                    navController = navController
                )
            }
        }
        else -> {
            AuthNavHost(
                authViewModel = viewModel,
                navController = navController
            )
        }
    }
}