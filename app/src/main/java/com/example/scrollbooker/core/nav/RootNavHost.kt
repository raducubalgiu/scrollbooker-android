package com.example.scrollbooker.core.nav
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import com.example.scrollbooker.core.nav.host.AuthNavHost
import com.example.scrollbooker.core.nav.host.MainNavHost
import com.example.scrollbooker.core.nav.routes.GlobalRoute
import com.example.scrollbooker.feature.auth.presentation.AuthViewModel
import com.example.scrollbooker.feature.auth.presentation.LoginState

@Composable
fun RootNavHost(navController: NavHostController) {
    val viewModel: AuthViewModel = hiltViewModel()
    val loginState by viewModel.loginState.collectAsState()
    val isInitialized by viewModel.isInitialized.collectAsState()

    if(isInitialized) {
        val startDestination = when(loginState) {
            is LoginState.Success -> GlobalRoute.MAIN
            else -> GlobalRoute.AUTH
        }

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