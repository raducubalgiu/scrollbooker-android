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

@Composable
fun RootNavHost(navController: NavHostController) {
    val viewModel: AuthViewModel = hiltViewModel()
    val startDestination by viewModel.startDestination.collectAsState()

    startDestination.let { destination ->
        NavHost(
            navController = navController,
            startDestination = destination!!
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