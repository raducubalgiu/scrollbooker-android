package com.example.scrollbooker.navigation.graphs

import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.scrollbooker.R
import com.example.scrollbooker.navigation.routes.OnboardingRoute
import com.example.scrollbooker.ui.auth.AuthViewModel
import com.example.scrollbooker.ui.onboarding.business.collectBusiness.CollectBusinessDetailsScreen
import com.example.scrollbooker.ui.onboarding.business.collectGallery.CollectBusinessGalleryScreen
import com.example.scrollbooker.ui.onboarding.business.collectHasEmployees.CollectBusinessHasEmployeesScreen
import com.example.scrollbooker.ui.onboarding.business.collectHasEmployees.CollectBusinessHasEmployeesViewModel
import com.example.scrollbooker.ui.onboarding.business.collectBusiness.CollectBusinessLocationScreen
import com.example.scrollbooker.ui.onboarding.business.collectSchedules.CollectBusinessSchedulesScreen
import com.example.scrollbooker.ui.onboarding.business.collectSchedules.CollectBusinessSchedulesViewModel
import com.example.scrollbooker.ui.onboarding.business.collectServices.CollectBusinessServicesScreen
import com.example.scrollbooker.ui.onboarding.business.collectServices.CollectBusinessServicesViewModel
import com.example.scrollbooker.ui.onboarding.business.collectBusiness.CollectBusinessTypeScreen
import com.example.scrollbooker.ui.onboarding.business.collectValidation.CollectBusinessValidationScreen
import com.example.scrollbooker.ui.onboarding.business.collectBusiness.CollectBusinessViewModel
import com.example.scrollbooker.ui.onboarding.business.collectGallery.CollectBusinessGalleryViewModel
import com.example.scrollbooker.ui.onboarding.client.CollectClientBirthDateScreen
import com.example.scrollbooker.ui.onboarding.client.CollectClientBirthDateViewModel
import com.example.scrollbooker.ui.onboarding.client.CollectClientGenderScreen
import com.example.scrollbooker.ui.onboarding.client.CollectClientGenderViewModel
import com.example.scrollbooker.ui.onboarding.client.CollectClientLocationPermissionScreen
import com.example.scrollbooker.ui.onboarding.client.CollectClientLocationPermissionViewModel
import com.example.scrollbooker.ui.onboarding.shared.CollectEmailVerificationScreen
import com.example.scrollbooker.ui.onboarding.shared.CollectUserUsernameScreen
import com.example.scrollbooker.ui.onboarding.shared.CollectUserUsernameViewModel
import kotlinx.coroutines.launch

fun NavGraphBuilder.onBoardingGraph(
    authViewModel: AuthViewModel,
    navController: NavHostController
) {
    // Shared
    composable(OnboardingRoute.CollectEmailVerification.route) {
        CollectEmailVerificationScreen(
            onNext = {
                authViewModel.verifyEmail()
                navController.navigate(OnboardingRoute.CollectUserUsername.route)
            },
        )
    }

    composable(OnboardingRoute.CollectUserUsername.route) { backStackEntry ->
        val viewModel: CollectUserUsernameViewModel = hiltViewModel(backStackEntry)

        CollectUserUsernameScreen(
            viewModel = viewModel,
            onSubmit = {
                navController.currentBackStackEntry?.lifecycleScope?.launch {
                    val authState = viewModel.collectUserUsername(newUsername = it)

                    authState.onSuccess { authViewModel.updateAuthState(it) }
                }
            }
        )
    }

    composable(OnboardingRoute.CollectClientLocationPermission.route) { backStackEntry ->
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
    composable(OnboardingRoute.CollectClientBirthDate.route) { backStackEntry ->
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

    composable(OnboardingRoute.CollectClientGender.route) { backStackEntry ->
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
        route = OnboardingRoute.CollectBusiness.route,
        startDestination = OnboardingRoute.CollectBusinessType.route
    ) {
        composable(route = OnboardingRoute.CollectBusinessType.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(OnboardingRoute.CollectBusiness.route)
            }
            val viewModel: CollectBusinessViewModel = hiltViewModel(parentEntry)

            CollectBusinessTypeScreen(
                viewModel = viewModel,
                onNext = { navController.navigate(OnboardingRoute.CollectBusinessDetails.route) },
            )
        }

        composable(route = OnboardingRoute.CollectBusinessDetails.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(OnboardingRoute.CollectBusiness.route)
            }
            val viewModel: CollectBusinessViewModel = hiltViewModel(parentEntry)

            CollectBusinessDetailsScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onNext = { navController.navigate(OnboardingRoute.CollectBusinessLocation.route) }
            )
        }

        composable(route = OnboardingRoute.CollectBusinessLocation.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(OnboardingRoute.CollectBusiness.route)
            }
            val viewModel: CollectBusinessViewModel = hiltViewModel(parentEntry)

            CollectBusinessLocationScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onNextOrSave = {
                    navController.currentBackStackEntry?.lifecycleScope?.launch {
                        val business = viewModel.createBusiness()

                        business.onSuccess { authViewModel.updateAuthState(it.onboardingState) }
                    }
                }
            )
        }
    }

    composable(route = OnboardingRoute.CollectBusinessGallery.route) {
        val viewModel: CollectBusinessGalleryViewModel = hiltViewModel()

        CollectBusinessGalleryScreen(
            viewModel = viewModel,
            onBack = { navController.popBackStack() },
            onNext = { skipUpdateGallery ->
                navController.currentBackStackEntry?.lifecycleScope?.launch {
                    val authState = viewModel.collectBusinessGallery(skipUpdateGallery)

                    authState.onSuccess { authViewModel.updateAuthState(it) }
                }
            }
        )
    }

    composable(OnboardingRoute.CollectBusinessServices.route) { backStackEntry ->
        val viewModel: CollectBusinessServicesViewModel = hiltViewModel(backStackEntry)
        val buttonTitle = stringResource(R.string.nextStep)

        CollectBusinessServicesScreen(
            viewModel = viewModel,
            buttonTitle = buttonTitle,
            onNext = {
                navController.currentBackStackEntry?.lifecycleScope?.launch {
                    val authState = viewModel.updateBusinessServices()

                    authState.onSuccess { authViewModel.updateAuthState(it) }
                }
            },
        )
    }

    composable(OnboardingRoute.CollectBusinessSchedules.route) { backStackEntry ->
        val viewModel: CollectBusinessSchedulesViewModel = hiltViewModel(backStackEntry)

        CollectBusinessSchedulesScreen(
            viewModel = viewModel,
            onNext = {
                navController.currentBackStackEntry?.lifecycleScope?.launch {
                    val authState = viewModel.updateSchedules()

                    authState.onSuccess { authViewModel.updateAuthState(it) }
                }
            }
        )
    }

    composable(OnboardingRoute.CollectBusinessHasEmployees.route) { backStackEntry ->
        val viewModel: CollectBusinessHasEmployeesViewModel = hiltViewModel(backStackEntry)

        CollectBusinessHasEmployeesScreen(
            viewModel = viewModel,
            onNext = {
                navController.currentBackStackEntry?.lifecycleScope?.launch {
                    val authState = viewModel.updateHasEmployees()

                    authState.onSuccess { authViewModel.updateAuthState(it) }
                }
            }
        )
    }

    composable(OnboardingRoute.CollectBusinessValidation.route) { backStackEntry ->
        CollectBusinessValidationScreen()
    }
}