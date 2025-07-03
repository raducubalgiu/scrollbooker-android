package com.example.scrollbooker.core.nav.host
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.navigation.compose.navigation
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.auth.data.remote.RoleNameEnum
import com.example.scrollbooker.entity.auth.domain.model.AuthState
import com.example.scrollbooker.entity.user.userInfo.domain.model.RegistrationStepEnum
import com.example.scrollbooker.screens.auth.AuthScreen
import com.example.scrollbooker.screens.auth.CollectEmailVerificationScreen
import com.example.scrollbooker.screens.auth.CollectUserUsernameScreen
import com.example.scrollbooker.screens.auth.CollectUserUsernameViewModel
import com.example.scrollbooker.screens.auth.collectBusinessDetails.collectBusinessServices.MyServicesScreen
import com.example.scrollbooker.screens.auth.collectBusinessType.CollectBusinessTypeScreen
import com.example.scrollbooker.screens.auth.collectBusinessType.MyBusinessDetailsScreen
import com.example.scrollbooker.screens.auth.collectBusinessType.MyBusinessGalleryScreen
import com.example.scrollbooker.screens.auth.collectClientDetails.CollectClientBirthDateScreen
import com.example.scrollbooker.screens.auth.collectClientDetails.CollectClientBirthDateViewModel
import com.example.scrollbooker.screens.auth.collectClientDetails.CollectClientGenderScreen
import com.example.scrollbooker.screens.auth.collectClientDetails.CollectClientGenderViewModel
import com.example.scrollbooker.screens.auth.collectClientDetails.CollectClientLocationPermissionScreen
import com.example.scrollbooker.screens.auth.collectClientDetails.CollectClientLocationPermissionViewModel
import com.example.scrollbooker.screens.profile.myBusiness.myBusinessLocation.MyBusinessLocationScreen
import com.example.scrollbooker.screens.profile.myBusiness.myBusinessLocation.MyBusinessLocationViewModel
import com.example.scrollbooker.screens.profile.myBusiness.mySchedules.SchedulesScreen
import com.example.scrollbooker.screens.profile.myBusiness.mySchedules.MySchedulesViewModel
import com.example.scrollbooker.screens.profile.myBusiness.myServices.MyServicesViewModel
import kotlinx.coroutines.launch
import timber.log.Timber

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
                AuthScreen(
                    viewModel = authViewModel,
                    type = AuthTypeEnum.LOGIN,
                    onNavigate = { navController.navigate(it) },
                    onSubmit = { _, username, password ->
                        authViewModel.login(
                            username,
                            password
                        )
                    }
                )
            }

            composable(AuthRoute.RegisterClient.route) {
                AuthScreen(
                    viewModel = authViewModel,
                    type = AuthTypeEnum.REGISTER,
                    onNavigate = { navController.navigate(it) },
                    onSubmit = { email, _, password ->
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
                AuthScreen(
                    viewModel = authViewModel,
                    type = AuthTypeEnum.REGISTER_BUSINESS,
                    onBack = { navController.popBackStack() },
                    onNavigate = { navController.navigate(it) },
                    onSubmit = { email, _, password ->
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
                        coroutineScope.launch {
                           val authState = viewModel.collectUserBirthDate()
                            if(authState != null) {
                                authViewModel.updateAuthState(authState)
                            }
                        }
                    }
                )
            }

            composable(AuthRoute.CollectClientGender.route) { backStackEntry ->
                val viewModel: CollectClientGenderViewModel = hiltViewModel(backStackEntry)

                CollectClientGenderScreen(
                    viewModel = viewModel,
                    onNext = {
                        coroutineScope.launch {
                            val authState = viewModel.collectUserGender(it)
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

                        }
                    )
                }
            }

            composable(AuthRoute.CollectBusinessServices.route) { backStackEntry ->
                val viewModel: MyServicesViewModel = hiltViewModel(backStackEntry)

                MyServicesScreen(
                    viewModel = viewModel,
                    onBack = { navController.navigate(AuthRoute.CollectBusinessLocation.route) },
                    onNextOrSave = { navController.navigate(AuthRoute.CollectBusinessSchedules.route) },
                )
            }

            composable(AuthRoute.CollectBusinessSchedules.route) { backStackEntry ->
                val viewModel: MySchedulesViewModel = hiltViewModel(backStackEntry)

                SchedulesScreen(
                    viewModel = viewModel,
                    onBack = { navController.navigate(AuthRoute.CollectBusinessServices.route) },
                )
            }
        }
    }
}