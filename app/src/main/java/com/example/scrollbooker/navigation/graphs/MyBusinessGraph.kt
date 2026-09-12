package com.example.scrollbooker.navigation.graphs
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.example.scrollbooker.R
import com.example.scrollbooker.navigation.navigators.ProfileNavigator
import com.example.scrollbooker.navigation.routes.MainRoute
import com.example.scrollbooker.screens.auth.collectBusinessDetails.collectBusinessServices.MyServicesScreen
import com.example.scrollbooker.ui.LocalUserPermissions
import com.example.scrollbooker.ui.myBusiness.MyBusinessScreen
import com.example.scrollbooker.ui.myBusiness.MyBusinessViewModel
import com.example.scrollbooker.ui.myBusiness.unapprovedBusinesses.UnapprovedBusinessesScreen
import com.example.scrollbooker.ui.myBusiness.unapprovedBusinesses.UnapprovedBusinessesViewModel
import com.example.scrollbooker.ui.myBusiness.myBusinessDetails.MyBusinessDetailsScreen
import com.example.scrollbooker.ui.myBusiness.myBusinessDetails.MyBusinessDetailsViewModel
import com.example.scrollbooker.ui.myBusiness.myCalendar.MyCalendarScreen
import com.example.scrollbooker.ui.myBusiness.myCalendar.MyCalendarViewModel
import com.example.scrollbooker.ui.myBusiness.myCalendar.addOwnClient.AddOwnClientScreen
import com.example.scrollbooker.ui.myBusiness.myCalendar.addOwnClient.AddOwnClientViewModel
import com.example.scrollbooker.ui.myBusiness.myCalendar.settings.MyCalendarSettingsScreen
import com.example.scrollbooker.ui.myBusiness.myDashboard.MyDashboardScreen
import com.example.scrollbooker.ui.myBusiness.myDashboard.MyDashboardViewModel
import com.example.scrollbooker.ui.myBusiness.myEmployees.MyEmployeesViewModel
import com.example.scrollbooker.ui.myBusiness.myEmployees.MyEmployeesScreen
import com.example.scrollbooker.ui.myBusiness.myEmployees.tabs.employmentRequestsTab.EmploymentAcceptTermsScreen
import com.example.scrollbooker.ui.myBusiness.myEmployees.tabs.employmentRequestsTab.EmploymentAssignJobScreen
import com.example.scrollbooker.ui.myBusiness.myEmployees.tabs.employmentRequestsTab.EmploymentSelectEmployeeScreen
import com.example.scrollbooker.ui.myBusiness.myProducts.productState.AddProductScreen
import com.example.scrollbooker.ui.myBusiness.myProducts.productState.AddProductViewModel
import com.example.scrollbooker.ui.myBusiness.myProducts.productState.EditProductScreen
import com.example.scrollbooker.ui.myBusiness.myProducts.productState.EditProductViewModel
import com.example.scrollbooker.ui.myBusiness.myProducts.MyProductsScreen
import com.example.scrollbooker.ui.myBusiness.myProducts.MyProductsViewModel
import com.example.scrollbooker.ui.myBusiness.mySchedules.MySchedulesScreen
import com.example.scrollbooker.ui.myBusiness.mySchedules.MySchedulesViewModel
import com.example.scrollbooker.ui.myBusiness.myServices.MyServicesViewModel
import kotlinx.coroutines.launch

fun NavGraphBuilder.myBusinessGraph(
    navController: NavHostController,
    profileNavigate: ProfileNavigator
) {
    navigation(
        route = MainRoute.MyBusinessNavigator.route,
        startDestination = MainRoute.MyBusiness.route
    ) {
        composable(MainRoute.MyBusiness.route) {
            val permissionController = LocalUserPermissions.current
            val viewModel: MyBusinessViewModel = hiltViewModel()

            MyBusinessScreen(
                viewModel = viewModel,
                permissionsController = permissionController,
                profileNavigate = profileNavigate
            )
        }

        composable(MainRoute.MyDashboard.route) {
            val viewModel = hiltViewModel<MyDashboardViewModel>()

            MyDashboardScreen(
                viewModel = viewModel,
                profileNavigate = profileNavigate
            )
        }

        composable(MainRoute.UnapprovedBusinesses.route) {
            val viewModel = hiltViewModel<UnapprovedBusinessesViewModel>()

            UnapprovedBusinessesScreen(
                viewModel = viewModel,
                profileNavigate = profileNavigate
            )
        }

        composable(MainRoute.MyBusinessDetails.route) {
            val viewModel = hiltViewModel<MyBusinessDetailsViewModel>()

            MyBusinessDetailsScreen(
                viewModel = viewModel,
                profileNavigate = profileNavigate
            )
        }

        navigation(
            route = MainRoute.MyEmployeesNavigator.route,
            startDestination = MainRoute.MyEmployees.route,
            arguments = listOf(
                navArgument("tabIndex") {
                    type = NavType.IntType
                    nullable = false
                    defaultValue = 0
                }
            )
        ) {
            composable(MainRoute.MyEmployees.route) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(MainRoute.MyEmployeesNavigator.route)
                }

                val tabIndex = parentEntry.arguments?.getInt("tabIndex") ?: 0
                val viewModel = hiltViewModel<MyEmployeesViewModel>(parentEntry)

                MyEmployeesScreen(
                    viewModel = viewModel,
                    tabIndex = tabIndex,
                    profileNavigate = profileNavigate
                )
            }

            composable(MainRoute.EmploymentSelectEmployee.route) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(MainRoute.MyEmployeesNavigator.route)
                }

                val viewModel = hiltViewModel<MyEmployeesViewModel>(parentEntry)

                EmploymentSelectEmployeeScreen(
                    viewModel = viewModel,
                    profileNavigate = profileNavigate,
                )
            }

            composable(MainRoute.EmploymentAssignJob.route) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(MainRoute.MyEmployeesNavigator.route)
                }

                val viewModel = hiltViewModel<MyEmployeesViewModel>(parentEntry)

                EmploymentAssignJobScreen(
                    viewModel = viewModel,
                    profileNavigate = profileNavigate
                )
            }

            composable(MainRoute.EmploymentAcceptTerms.route) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(MainRoute.MyEmployeesNavigator.route)
                }

                val viewModel = hiltViewModel<MyEmployeesViewModel>(parentEntry)

                EmploymentAcceptTermsScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                    onNext = {
                        navController.currentBackStackEntry?.lifecycleScope?.launch {
                            val result = viewModel.createEmploymentRequest()
                            result
                                .onSuccess {
                                    navController.navigate("myEmployeesNavigator/1") {
                                        popUpTo(MainRoute.MyEmployeesNavigator.route) {
                                            inclusive = true
                                        }
                                    }
                                }
                        }
                    },
                )
            }
        }

        composable(MainRoute.MySchedules.route) { backStackEntry ->
            val viewModel = hiltViewModel<MySchedulesViewModel>(backStackEntry)
            val coroutineScope = rememberCoroutineScope()

            MySchedulesScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onNextOrSave = {
                    coroutineScope.launch {
                        viewModel.updateSchedules()
                    }
                }
            )
        }

        navigation(
            route = MainRoute.MyCalendarNavigator.route,
            startDestination = MainRoute.MyCalendar.route
        ) {
            val pushSpec: FiniteAnimationSpec<IntOffset> = tween(320, easing = LinearOutSlowInEasing)
            val popSpec: FiniteAnimationSpec<IntOffset> = tween(280, easing = LinearOutSlowInEasing)
            val fadeInSpec: FiniteAnimationSpec<Float> = tween(220, easing = LinearOutSlowInEasing)
            val fadeOutSpec: FiniteAnimationSpec<Float> = tween(220, easing = LinearOutSlowInEasing)

            composable(MainRoute.MyCalendar.route) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(MainRoute.MyCalendarNavigator.route)
                }
                val viewModel = hiltViewModel<MyCalendarViewModel>(parentEntry)

                MyCalendarScreen(
                    viewModel = viewModel,
                    profileNavigate = profileNavigate,
                    onBack = { navController.popBackStack() }
                )
            }

            composable(
                route = MainRoute.AddOwnClientAppointment.route,
                enterTransition = { slideInVertically(pushSpec) { it } + fadeIn(fadeInSpec) },
                exitTransition = { ExitTransition.None },
                popEnterTransition = { EnterTransition.None },
                popExitTransition = { slideOutVertically(popSpec) { it } + fadeOut(fadeOutSpec) }
            ) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(MainRoute.MyCalendarNavigator.route)
                }
                val myCalendarViewModel = hiltViewModel<MyCalendarViewModel>(parentEntry)
                val viewModel = hiltViewModel<AddOwnClientViewModel>()

                AddOwnClientScreen(
                    myCalendarViewModel = myCalendarViewModel,
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                )
            }

            composable(
                route = MainRoute.MyCalendarSettings.route,
                enterTransition = { slideInVertically(pushSpec) { it } + fadeIn(fadeInSpec) },
                exitTransition = { ExitTransition.None },
                popEnterTransition = { EnterTransition.None },
                popExitTransition = { slideOutVertically(popSpec) { it } + fadeOut(fadeOutSpec) }
            ) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(MainRoute.MyCalendarNavigator.route)
                }
                val myCalendarViewModel = hiltViewModel<MyCalendarViewModel>(parentEntry)

                MyCalendarSettingsScreen(
                    myCalendarViewModel = myCalendarViewModel,
                    onBack = { navController.popBackStack() }
                )
            }
        }

        composable(MainRoute.MyServices.route) { backStackEntry ->
            val viewModel = hiltViewModel<MyServicesViewModel>(backStackEntry)
            val coroutineScope = rememberCoroutineScope()

            MyServicesScreen(
                viewModel = viewModel,
                buttonTitle = stringResource(R.string.save),
                onBack = { navController.popBackStack() },
                onNextOrSave = {
                    coroutineScope.launch {
                        viewModel.updateBusinessServices()
                    }
                }
            )
        }

        navigation(
            route = MainRoute.MyProductsNavigator.route,
            startDestination = MainRoute.MyProducts.route
        ) {
            composable(MainRoute.MyProducts.route) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(MainRoute.MyProductsNavigator.route)
                }

                val viewModel: MyProductsViewModel = hiltViewModel(parentEntry)

                MyProductsScreen(
                    viewModel = viewModel,
                    profileNavigate = profileNavigate,
                )
            }

            composable(route = MainRoute.AddProduct.route) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(MainRoute.MyProductsNavigator.route)
                }

                val myProductsViewModel: MyProductsViewModel = hiltViewModel(parentEntry)
                val viewModel: AddProductViewModel = hiltViewModel()

                AddProductScreen(
                    myProductsViewModel = myProductsViewModel,
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                )
            }

            composable(
                route = MainRoute.EditProduct.route,
                arguments = listOf(
                    navArgument("productId") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(MainRoute.MyProductsNavigator.route)
                }

                val myProductsViewModel: MyProductsViewModel = hiltViewModel(parentEntry)
                val viewModel: EditProductViewModel = hiltViewModel()

                EditProductScreen(
                    myProductsViewModel = myProductsViewModel,
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}