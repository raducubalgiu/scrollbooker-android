package com.example.scrollbooker.navigation.host
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.navigation
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.navigation.graphs.authGraph
import com.example.scrollbooker.navigation.graphs.mainGraph
import com.example.scrollbooker.navigation.graphs.onBoardingGraph
import com.example.scrollbooker.navigation.routes.AuthRoute
import com.example.scrollbooker.navigation.routes.OnboardingRoute
import com.example.scrollbooker.navigation.routes.RootRoute
import com.example.scrollbooker.ui.auth.AuthViewModel
import kotlinx.coroutines.launch

@Composable
fun RootNavHost(
    rootNavController: NavHostController,
    authViewModel: AuthViewModel
) {
    val authState by authViewModel.authState.collectAsState()
    val scope = rememberCoroutineScope()

    val startDestination = when (val s = authState) {
        is FeatureState.Success -> when {
            s.data.registrationStep != null -> RootRoute.ONBOARDING
            s.data.isValidated -> RootRoute.MAIN
            else -> RootRoute.AUTH
        }
        else -> RootRoute.AUTH
    }

    val onboardingStepKey =
        (authState as? FeatureState.Success)?.data?.registrationStep?.key
            ?: OnboardingRoute.CollectEmailVerification.route

    key(startDestination) {
        NavHost(
            navController = rootNavController,
            startDestination = startDestination
        ) {
            navigation(
                route = RootRoute.AUTH,
                startDestination = AuthRoute.Login.route
            ) { authGraph(authViewModel, rootNavController) }

            navigation(
                route = RootRoute.ONBOARDING,
                startDestination = onboardingStepKey
            ) { onBoardingGraph(authViewModel, rootNavController) }

            mainGraph(
                onLogout = {
                    scope.launch {
                        if(authViewModel.logout().isSuccess) {
                            rootNavController.navigate(RootRoute.AUTH) {
                                popUpTo(0) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    }
                }
            )
        }
    }
}