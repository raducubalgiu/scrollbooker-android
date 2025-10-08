package com.example.scrollbooker.navigation.host

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.scrollbooker.R
import com.example.scrollbooker.navigation.routes.AuthRoute
import com.example.scrollbooker.ui.auth.AuthViewModel
import com.example.scrollbooker.ui.myBusiness.myBusinessLocation.MyBusinessLocationViewModel
import com.example.scrollbooker.ui.onboarding.business.CollectBusinessDetailsScreen
import com.example.scrollbooker.ui.onboarding.business.CollectBusinessGalleryScreen
import com.example.scrollbooker.ui.onboarding.business.CollectBusinessHasEmployeesScreen
import com.example.scrollbooker.ui.onboarding.business.CollectBusinessHasEmployeesViewModel
import com.example.scrollbooker.ui.onboarding.business.CollectBusinessLocationScreen
import com.example.scrollbooker.ui.onboarding.business.CollectBusinessSchedulesScreen
import com.example.scrollbooker.ui.onboarding.business.CollectBusinessSchedulesViewModel
import com.example.scrollbooker.ui.onboarding.business.CollectBusinessServicesScreen
import com.example.scrollbooker.ui.onboarding.business.CollectBusinessServicesViewModel
import com.example.scrollbooker.ui.onboarding.business.CollectBusinessTypeScreen
import com.example.scrollbooker.ui.onboarding.business.CollectBusinessValidationScreen
import com.example.scrollbooker.ui.onboarding.client.CollectClientBirthDateScreen
import com.example.scrollbooker.ui.onboarding.client.CollectClientBirthDateViewModel
import com.example.scrollbooker.ui.onboarding.client.CollectClientGenderScreen
import com.example.scrollbooker.ui.onboarding.client.CollectClientGenderViewModel
import com.example.scrollbooker.ui.onboarding.client.CollectClientLocationPermissionScreen
import com.example.scrollbooker.ui.onboarding.client.CollectClientLocationPermissionViewModel
import com.example.scrollbooker.ui.onboarding.shared.CollectUserUsernameScreen
import com.example.scrollbooker.ui.onboarding.shared.CollectUserUsernameViewModel
import kotlinx.coroutines.launch

@Composable
fun OnboardingNavHost(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    NavHost(
        navController = navController,
        startDestination = AuthRoute.CollectUserUsername.route
    ) {
        // Shared
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

        // Client
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

        // Business
        navigation(
            route = AuthRoute.CollectBusiness.route,
            startDestination = AuthRoute.CollectBusinessType.route
        ) {
            composable(route = AuthRoute.CollectBusinessType.route) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(AuthRoute.CollectBusiness.route)
                }
                val viewModel: MyBusinessLocationViewModel = hiltViewModel(parentEntry)

                CollectBusinessTypeScreen(
                    viewModel = viewModel,
                    onNext = { navController.navigate(AuthRoute.CollectBusinessDetails.route) },
                )
            }

            composable(route = AuthRoute.CollectBusinessDetails.route) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(AuthRoute.CollectBusiness.route)
                }
                val viewModel: MyBusinessLocationViewModel = hiltViewModel(parentEntry)

                CollectBusinessDetailsScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                    onNext = { navController.navigate(AuthRoute.CollectBusinessLocation.route) }
                )
            }

            composable(route = AuthRoute.CollectBusinessLocation.route) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(AuthRoute.CollectBusiness.route)
                }
                val viewModel: MyBusinessLocationViewModel = hiltViewModel(parentEntry)

                CollectBusinessLocationScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                    onNextOrSave = {
                        navController.navigate(AuthRoute.CollectBusinessGallery.route)
                    }
                )
            }

            composable(route = AuthRoute.CollectBusinessGallery.route) { backStackEntry ->
                val scope = rememberCoroutineScope()
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(AuthRoute.CollectBusiness.route)
                }
                val viewModel: MyBusinessLocationViewModel = hiltViewModel(parentEntry)

                CollectBusinessGalleryScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                    onNext = {
                        scope.launch {
                            val business = viewModel.createBusiness()

                            if(business != null) {
                                authViewModel.updateAuthState(business.authState)
                            }
                        }
                    }
                )
            }
        }

        composable(AuthRoute.CollectBusinessServices.route) { backStackEntry ->
            val scope = rememberCoroutineScope()
            val viewModel: CollectBusinessServicesViewModel = hiltViewModel(backStackEntry)
            val buttonTitle = stringResource(R.string.nextStep)

            CollectBusinessServicesScreen(
                viewModel = viewModel,
                buttonTitle = buttonTitle,
                onBack = { navController.navigate(AuthRoute.CollectBusinessLocation.route) },
                onNextOrSave = {
                    scope.launch {
                        val authState = viewModel.updateBusinessServices()
                        if(authState != null) {
                            authViewModel.updateAuthState(authState)
                        }
                    }
                },
            )
        }

        composable(AuthRoute.CollectBusinessSchedules.route) { backStackEntry ->
            val scope = rememberCoroutineScope()
            val viewModel: CollectBusinessSchedulesViewModel = hiltViewModel(backStackEntry)

            CollectBusinessSchedulesScreen(
                viewModel = viewModel,
                onBack = { navController.navigate(AuthRoute.CollectBusinessServices.route) },
                onNextOrSave = {
                    scope.launch {
                        val authState = viewModel.updateSchedules()
                        if(authState != null) {
                            authViewModel.updateAuthState(authState)
                        }
                    }
                }
            )
        }

        composable(AuthRoute.CollectBusinessHasEmployees.route) { backStackEntry ->
            val scope = rememberCoroutineScope()
            val viewModel: CollectBusinessHasEmployeesViewModel = hiltViewModel(backStackEntry)

            CollectBusinessHasEmployeesScreen(
                viewModel = viewModel,
                onNext = {
                    scope.launch {
                        val authState = viewModel.updateHasEmployees()
                        if(authState != null) {
                            authViewModel.updateAuthState(authState)
                        }
                    }
                }
            )
        }

        composable(AuthRoute.CollectBusinessValidation.route) { backStackEntry ->
            CollectBusinessValidationScreen()
        }
    }
}