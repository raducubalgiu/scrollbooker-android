package com.example.scrollbooker.core.nav
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import com.example.scrollbooker.core.nav.host.AuthNavHost
import com.example.scrollbooker.core.nav.host.MainNavHost
import com.example.scrollbooker.core.nav.routes.GlobalRoute
import com.example.scrollbooker.feature.auth.presentation.AuthViewModel
import com.example.scrollbooker.feature.auth.presentation.FeatureState

@Composable
fun RootNavHost(
    navController: NavHostController,
    viewModel: AuthViewModel
) {
    val loginState by viewModel.loginState.collectAsState()

    val startDestination = when(loginState) {
        is FeatureState.Success -> GlobalRoute.MAIN
        is FeatureState.Error -> GlobalRoute.AUTH
        else -> null
    }

    if(startDestination != null) {
        NavHost(
            navController = navController,
            startDestination = startDestination
        ) {
            composable(GlobalRoute.AUTH) {
                AuthNavHost(viewModel)
            }

            composable(GlobalRoute.MAIN) {
                MainNavHost()
            }
        }
    }
}