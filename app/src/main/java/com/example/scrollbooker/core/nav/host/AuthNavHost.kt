package com.example.scrollbooker.core.nav.host
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
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.screens.auth.AuthScreen
import com.example.scrollbooker.screens.auth.CollectEmailVerificationScreen
import com.example.scrollbooker.screens.auth.CollectUserUsernameScreen
import com.example.scrollbooker.screens.auth.collectBusinessDetails.collectBusinessServices.MyServicesScreen
import com.example.scrollbooker.screens.auth.collectBusinessType.CollectBusinessTypeScreen
import com.example.scrollbooker.screens.auth.collectBusinessType.CollectBusinessTypeViewModel
import com.example.scrollbooker.screens.auth.collectClientDetails.CollectClientBirthDateScreen
import com.example.scrollbooker.screens.auth.collectClientDetails.CollectClientGenderScreen
import com.example.scrollbooker.screens.profile.myBusiness.myBusinessLocation.MyBusinessLocationScreen
import com.example.scrollbooker.screens.profile.myBusiness.myBusinessLocation.MyBusinessLocationViewModel
import com.example.scrollbooker.screens.profile.myBusiness.mySchedules.SchedulesScreen
import com.example.scrollbooker.screens.profile.myBusiness.mySchedules.MySchedulesViewModel
import com.example.scrollbooker.screens.profile.myBusiness.myServices.MyServicesViewModel

enum class AuthTypeEnum {
    LOGIN,
    REGISTER
}

@Composable
fun AuthNavHost(viewModel: AuthViewModel) {
    val navController = rememberNavController()
    val authState by viewModel.authState.collectAsState()

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
                    viewModel = viewModel,
                    type = AuthTypeEnum.LOGIN,
                    onNavigate = { navController.navigate(it) },
                    onSubmit = { _, username, password ->
                        viewModel.login(
                            username,
                            password
                        )
                    }
                )
            }

            composable(AuthRoute.RegisterClient.route) {
                AuthScreen(
                    viewModel = viewModel,
                    type = AuthTypeEnum.REGISTER,
                    onNavigate = { navController.navigate(it) },
                    onSubmit = { email, _, password ->
                        viewModel.register(
                            email,
                            password
                        )
                    }
                )
            }

            composable(AuthRoute.RegisterBusiness.route) {
//                RegisterBusinessScreen(
//                    viewModel=viewModel,
//                    onNavigate = { navController.navigate(it) }
//                )
            }

            composable(AuthRoute.CollectEmailVerification.route) {
                CollectEmailVerificationScreen(
                    onNext = {
                        viewModel.verifyEmail()
                        navController.navigate(AuthRoute.CollectUserUsername.route)
                    },
                )
            }

            composable(AuthRoute.CollectUserUsername.route) {
                CollectUserUsernameScreen()
            }

            composable(AuthRoute.CollectClientBirthDate.route) {
                CollectClientBirthDateScreen(
                    onBack = {},
                    onNext = { navController.navigate(AuthRoute.CollectClientGender.route) }
                )
            }

            composable(AuthRoute.CollectClientGender.route) {
                CollectClientGenderScreen(
                    onBack = { navController.navigate(AuthRoute.CollectClientBirthDate.route) },
                    onNext = {}
                )
            }

            composable(route = AuthRoute.CollectBusinessType.route) { backStackEntry ->
                val viewModel: CollectBusinessTypeViewModel = hiltViewModel(backStackEntry)

                CollectBusinessTypeScreen(
                    viewModel = viewModel,
                    onNext = { navController.navigate(it) },
                )
            }

            composable(
                route = AuthRoute.CollectBusinessLocation.route,
            ) { backStackEntry ->
                val viewModel: MyBusinessLocationViewModel = hiltViewModel(backStackEntry)

                MyBusinessLocationScreen(
                    viewModel = viewModel,
                    onBack = { navController.navigate(AuthRoute.CollectBusinessType.route) },
                    onNextOrSave = { navController.navigate(AuthRoute.CollectBusinessServices.route) }
                )
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