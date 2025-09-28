package com.example.scrollbooker.navigation.graphs

import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.scrollbooker.navigation.routes.AuthRoute
import com.example.scrollbooker.ui.auth.AuthViewModel
import com.example.scrollbooker.ui.onboarding.client.CollectClientBirthDateScreen
import com.example.scrollbooker.ui.onboarding.client.CollectClientBirthDateViewModel
import com.example.scrollbooker.ui.onboarding.client.CollectClientGenderScreen
import com.example.scrollbooker.ui.onboarding.client.CollectClientGenderViewModel
import com.example.scrollbooker.ui.onboarding.shared.CollectClientLocationPermissionScreen
import com.example.scrollbooker.ui.onboarding.shared.CollectClientLocationPermissionViewModel
import com.example.scrollbooker.ui.onboarding.shared.CollectUserUsernameScreen
import com.example.scrollbooker.ui.onboarding.shared.CollectUserUsernameViewModel
import kotlinx.coroutines.launch

fun NavGraphBuilder.onBoardingGraph(
    authViewModel: AuthViewModel,
    navController: NavHostController
) {
    composable(AuthRoute.CollectUserUsername.route) { backStackEntry ->
        val scope = rememberCoroutineScope()
        val viewModel: CollectUserUsernameViewModel = hiltViewModel(backStackEntry)

        CollectUserUsernameScreen(
            viewModel = viewModel,
            onSubmit = {
                scope.launch {
                    val authState = viewModel.collectUserUsername(newUsername = it)
                    if(authState != null) {
                        authViewModel.updateAuthState(authState)
                    }
                }
            }
        )
    }

    composable(AuthRoute.CollectClientBirthDate.route) { backStackEntry ->
        val viewModel: CollectClientBirthDateViewModel = hiltViewModel(backStackEntry)

        CollectClientBirthDateScreen(
            viewModel = viewModel,
            onNext = {
                navController.currentBackStackEntry?.lifecycleScope?.launch {
                    val authState = viewModel.collectUserBirthDate()

                    authState.onSuccess { authViewModel.updateAuthState(it) }
                }
            }
        )
    }

    composable(AuthRoute.CollectClientGender.route) { backStackEntry ->
        val viewModel: CollectClientGenderViewModel = hiltViewModel(backStackEntry)

        CollectClientGenderScreen(
            viewModel = viewModel,
            onNext = {
                navController.currentBackStackEntry?.lifecycleScope?.launch {
                    val authState = viewModel.collectUserGender(it)

                    authState.onSuccess { authViewModel.updateAuthState(it) }
                }
            }
        )
    }

    composable(AuthRoute.CollectClientLocationPermission.route) { backStackEntry ->
        val viewModel: CollectClientLocationPermissionViewModel = hiltViewModel(backStackEntry)

        CollectClientLocationPermissionScreen(
            viewModel = viewModel,
            onNext = {
                navController.currentBackStackEntry?.lifecycleScope?.launch {
                    val authState = viewModel.collectLocationPermission()

                    authState.onSuccess { authViewModel.updateAuthState(it) }
                }
            }
        )
    }
}