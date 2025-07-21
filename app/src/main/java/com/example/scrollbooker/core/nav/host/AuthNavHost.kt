package com.example.scrollbooker.core.nav.host
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.scrollbooker.core.nav.routes.AuthRoute
import com.example.scrollbooker.screens.auth.AuthViewModel
import com.example.scrollbooker.ui.theme.Background
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.navigation
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.auth.data.remote.RoleNameEnum
import com.example.scrollbooker.screens.onboarding.business.CollectBusinessHasEmployeesScreen
import com.example.scrollbooker.screens.onboarding.business.CollectBusinessHasEmployeesViewModel
import com.example.scrollbooker.screens.onboarding.business.CollectBusinessValidationScreen
import com.example.scrollbooker.screens.onboarding.CollectEmailVerificationScreen
import com.example.scrollbooker.screens.onboarding.collectUsername.CollectUserUsernameScreen
import com.example.scrollbooker.screens.onboarding.collectUsername.CollectUserUsernameViewModel
import com.example.scrollbooker.screens.auth.LoginScreen
import com.example.scrollbooker.screens.auth.RegisterBusinessScreen
import com.example.scrollbooker.screens.auth.RegisterClientScreen
import com.example.scrollbooker.screens.auth.collectBusinessDetails.collectBusinessServices.MyServicesScreen
import com.example.scrollbooker.screens.onboarding.business.CollectBusinessTypeScreen
import com.example.scrollbooker.screens.onboarding.business.MyBusinessDetailsScreen
import com.example.scrollbooker.screens.onboarding.business.MyBusinessGalleryScreen
import com.example.scrollbooker.screens.onboarding.client.collectBirthdate.CollectClientBirthDateScreen
import com.example.scrollbooker.screens.onboarding.client.collectBirthdate.CollectClientBirthDateViewModel
import com.example.scrollbooker.screens.onboarding.client.collectGender.CollectClientGenderScreen
import com.example.scrollbooker.screens.onboarding.client.collectGender.CollectClientGenderViewModel
import com.example.scrollbooker.screens.onboarding.client.CollectClientLocationPermissionScreen
import com.example.scrollbooker.screens.onboarding.client.CollectClientLocationPermissionViewModel
import com.example.scrollbooker.screens.profile.myBusiness.myBusinessLocation.MyBusinessLocationScreen
import com.example.scrollbooker.screens.profile.myBusiness.myBusinessLocation.MyBusinessLocationViewModel
import com.example.scrollbooker.screens.profile.myBusiness.mySchedules.SchedulesScreen
import com.example.scrollbooker.screens.profile.myBusiness.mySchedules.MySchedulesViewModel
import com.example.scrollbooker.screens.profile.myBusiness.myServices.MyServicesViewModel
import kotlinx.coroutines.launch

enum class AuthTypeEnum {
    LOGIN,
    REGISTER,
    REGISTER_BUSINESS
}

@Composable
fun AuthNavHost(authViewModel: AuthViewModel) {
    val navController = rememberNavController()
    val authState by authViewModel.authState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    val startDestination = when(val state = authState) {
        is FeatureState.Success -> {
            val isValidated = state.data.isValidated
            val registrationStep = state.data.registrationStep?.key

            if(!isValidated && registrationStep != null) {
                registrationStep
            } else {
                AuthRoute.Login.route
            }
        }
        else -> AuthRoute.Login.route
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Background)
        .statusBarsPadding()
    ) {
        NavHost(
            navController = navController,
            startDestination = startDestination
        ) {
            composable(AuthRoute.Login.route) {
                LoginScreen(
                    viewModel = authViewModel,
                    onNavigateToRegisterClient = { navController.navigate(AuthRoute.RegisterClient.route) },
                    onNavigateToRegisterBusiness = { navController.navigate(AuthRoute.RegisterBusiness.route) },
                    onSubmit = { username, password ->
                        authViewModel.login(
                            username,
                            password
                        )
                    }
                )
            }

            composable(AuthRoute.RegisterClient.route) {
                RegisterClientScreen(
                    viewModel = authViewModel,
                    onNavigateToLogin = { navController.navigate(AuthRoute.Login.route) },
                    onNavigateToRegisterBusiness = { navController.navigate(AuthRoute.RegisterBusiness.route) },
                    onSubmit = { email, password ->
                        authViewModel.register(
                            email,
                            password,
                            roleName = RoleNameEnum.CLIENT
                        )
                    }
                )
            }

            composable(
                AuthRoute.RegisterBusiness.route,
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(
                            durationMillis = 250,
                            easing = FastOutSlowInEasing
                        )
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(
                            durationMillis = 250,
                            easing = FastOutSlowInEasing
                        )
                    )
                },
                popEnterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(
                            durationMillis = 250,
                            easing = FastOutSlowInEasing
                        )
                    )
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(
                            durationMillis = 250,
                            easing = FastOutSlowInEasing
                        )
                    )
                }
            ) {
                RegisterBusinessScreen(
                    viewModel = authViewModel,
                    onBack = { navController.popBackStack() },
                    onSubmit = { email, password ->
                        authViewModel.register(
                            email,
                            password,
                            roleName = RoleNameEnum.BUSINESS
                        )
                    }
                )
            }

            composable(AuthRoute.CollectEmailVerification.route) {
                CollectEmailVerificationScreen(
                    onNext = {
                        authViewModel.verifyEmail()
                        navController.navigate(AuthRoute.CollectUserUsername.route)
                    },
                )
            }

            composable(AuthRoute.CollectUserUsername.route) { backStackEntry ->
                val viewModel: CollectUserUsernameViewModel = hiltViewModel(backStackEntry)

                CollectUserUsernameScreen(
                    viewModel = viewModel,
                    onSubmit = {
                        coroutineScope.launch {
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
                    onNext = {}
                )
            }

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

                    MyBusinessDetailsScreen(
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

                    MyBusinessLocationScreen(
                        viewModel = viewModel,
                        onBack = { navController.popBackStack() },
                        onNextOrSave = {
                            navController.navigate(AuthRoute.CollectBusinessGallery.route)
                        }
                    )
                }

                composable(route = AuthRoute.CollectBusinessGallery.route) { backStackEntry ->
                    val parentEntry = remember(backStackEntry) {
                        navController.getBackStackEntry(AuthRoute.CollectBusiness.route)
                    }
                    val viewModel: MyBusinessLocationViewModel = hiltViewModel(parentEntry)

                    MyBusinessGalleryScreen(
                        viewModel = viewModel,
                        onBack = { navController.popBackStack() },
                        onNext = {
                            coroutineScope.launch {
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
                val viewModel: MyServicesViewModel = hiltViewModel(backStackEntry)
                val buttonTitle = stringResource(R.string.nextStep)

                MyServicesScreen(
                    viewModel = viewModel,
                    buttonTitle = buttonTitle,
                    onBack = { navController.navigate(AuthRoute.CollectBusinessLocation.route) },
                    onNextOrSave = {
                        coroutineScope.launch {
                            val authState = viewModel.updateBusinessServices()
                            if(authState != null) {
                                authViewModel.updateAuthState(authState)
                            }
                        }
                    },
                )
            }

            composable(AuthRoute.CollectBusinessSchedules.route) { backStackEntry ->
                val viewModel: MySchedulesViewModel = hiltViewModel(backStackEntry)

                SchedulesScreen(
                    viewModel = viewModel,
                    onBack = { navController.navigate(AuthRoute.CollectBusinessServices.route) },
                    onNextOrSave = {
                        coroutineScope.launch {
                            val authState = viewModel.updateSchedules()
                            if(authState != null) {
                                authViewModel.updateAuthState(authState)
                            }
                        }
                    }
                )
            }

            composable(AuthRoute.CollectBusinessHasEmployees.route) { backStackEntry ->
                val viewModel: CollectBusinessHasEmployeesViewModel = hiltViewModel(backStackEntry)

                CollectBusinessHasEmployeesScreen(
                    viewModel = viewModel,
                    onNext = {
                        coroutineScope.launch {
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
}