package com.example.scrollbooker.core.nav.navigators

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.scrollbooker.R
import com.example.scrollbooker.core.nav.routes.MainRoute
import com.example.scrollbooker.entity.employmentRequest.data.mappers.toDto
import com.example.scrollbooker.screens.profile.myBusiness.MyBusinessScreen
import com.example.scrollbooker.screens.profile.myBusiness.MyBusinessViewModel
import com.example.scrollbooker.screens.profile.myBusiness.myCalendar.MyCalendarScreen
import com.example.scrollbooker.screens.profile.myBusiness.myCalendar.MyCalendarViewModel
import com.example.scrollbooker.feature.myBusiness.employeeDismissal.presentation.EmployeesDismissalScreen
import com.example.scrollbooker.feature.myBusiness.employeeDismissal.presentation.EmployeesDismissalViewModel
import com.example.scrollbooker.screens.auth.collectBusinessDetails.collectBusinessServices.MyServicesScreen
import com.example.scrollbooker.screens.profile.myBusiness.myEmployees.EmployeesScreen
import com.example.scrollbooker.screens.profile.myBusiness.myEmployees.EmployeesViewModel
import com.example.scrollbooker.screens.profile.myBusiness.myEmploymentRequests.list.EmploymentRequestsScreen
import com.example.scrollbooker.screens.profile.myBusiness.myEmploymentRequests.list.EmploymentRequestsViewModel
import com.example.scrollbooker.screens.profile.myBusiness.myProducts.AddProductScreen
import com.example.scrollbooker.screens.profile.myBusiness.myProducts.MyProductsScreen
import com.example.scrollbooker.screens.profile.myBusiness.myCurrencies.MyCurrenciesScreen
import com.example.scrollbooker.screens.profile.myBusiness.myCurrencies.MyCurrenciesViewModel
import com.example.scrollbooker.screens.profile.myBusiness.myEmploymentRequests.flow.EmploymentRequestViewModel
import com.example.scrollbooker.screens.profile.myBusiness.myEmploymentRequests.flow.acceptTerms.EmploymentAcceptTermsScreen
import com.example.scrollbooker.screens.profile.myBusiness.myEmploymentRequests.flow.acceptTerms.EmploymentAcceptTermsViewModel
import com.example.scrollbooker.screens.profile.myBusiness.myEmploymentRequests.flow.assignJob.EmploymentAssignJobScreen
import com.example.scrollbooker.screens.profile.myBusiness.myEmploymentRequests.flow.assignJob.EmploymentAssignJobViewModel
import com.example.scrollbooker.screens.profile.myBusiness.myEmploymentRequests.flow.selectEmployee.EmploymentSelectEmployeeScreen
import com.example.scrollbooker.screens.profile.myBusiness.myEmploymentRequests.flow.selectEmployee.EmploymentSelectEmployeeViewModel
import com.example.scrollbooker.screens.profile.myBusiness.myProducts.EditProductScreen
import com.example.scrollbooker.screens.profile.myBusiness.myProducts.MyProductsViewModel
import com.example.scrollbooker.screens.profile.myBusiness.mySchedules.SchedulesScreen
import com.example.scrollbooker.screens.profile.myBusiness.mySchedules.MySchedulesViewModel
import com.example.scrollbooker.screens.profile.myBusiness.myServices.MyServicesViewModel
import kotlinx.coroutines.launch

fun NavGraphBuilder.myBusinessGraph(navController: NavHostController) {
    navigation(
        route = MainRoute.MyBusinessNavigator.route,
        startDestination = MainRoute.MyBusiness.route,
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
        composable(MainRoute.MyBusiness.route) {
            val viewModel = hiltViewModel<MyBusinessViewModel>()

            MyBusinessScreen(
                viewModel = viewModel,
                onNavigation = { navController.navigate(it) },
                onBack = { navController.popBackStack() }
            )
        }

        composable(MainRoute.Employees.route) { backStackEntry ->
            val viewModel = hiltViewModel<EmployeesViewModel>(backStackEntry)

            EmployeesScreen(
                viewModel,
                onBack = { navController.popBackStack() },
                onNavigate = { navController.navigate(it) }
            )
        }

        composable("${MainRoute.EmployeesDismissal.route}/{userId}",
            arguments = listOf(
                navArgument("userId") { type = NavType.IntType },
            )
        ) { backStackEntry ->
            val viewModel = hiltViewModel<EmployeesDismissalViewModel>(backStackEntry)

            EmployeesDismissalScreen(
                viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(MainRoute.EmploymentsRequests.route) { backStackEntry ->
            val viewModel = hiltViewModel<EmploymentRequestsViewModel>(backStackEntry)

            EmploymentRequestsScreen(
                viewModel,
                onBack = { navController.popBackStack() },
                onNavigateSelectEmployee = { navController.navigate(MainRoute.EmploymentRequestsFlow.route) }
            )
        }

        navigation(
            route = MainRoute.EmploymentRequestsFlow.route,
            startDestination = MainRoute.EmploymentSelectEmployee.route,
        ) {
            composable(MainRoute.EmploymentSelectEmployee.route) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(MainRoute.EmploymentSelectEmployee.route)
                }
                val employmentRequestViewModel: EmploymentRequestViewModel = hiltViewModel(parentEntry)
                val employmentSelectEmployeeViewModel: EmploymentSelectEmployeeViewModel = hiltViewModel()

                EmploymentSelectEmployeeScreen(
                    globalViewModel = employmentRequestViewModel,
                    localViewModel = employmentSelectEmployeeViewModel,
                    onNext = { navController.navigate(MainRoute.EmploymentAssignJob.route) },
                    onBack = { navController.popBackStack() }
                )
            }

            composable(MainRoute.EmploymentAssignJob.route) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(MainRoute.EmploymentSelectEmployee.route)
                }
                val employmentRequestViewModel: EmploymentRequestViewModel = hiltViewModel(parentEntry)
                val employmentAssignJobViewModel: EmploymentAssignJobViewModel = hiltViewModel()

                EmploymentAssignJobScreen(
                    globalViewModel = employmentRequestViewModel,
                    localViewModel = employmentAssignJobViewModel,
                    onNext = { navController.navigate(MainRoute.EmploymentAcceptTerms.route) },
                    onBack = { navController.popBackStack() }
                )
            }

            composable(MainRoute.EmploymentAcceptTerms.route) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(MainRoute.EmploymentSelectEmployee.route)
                }
                val employmentRequestViewModel: EmploymentRequestViewModel = hiltViewModel(parentEntry)
                val employmentAcceptTermsViewModel: EmploymentAcceptTermsViewModel = hiltViewModel()
                val employmentRequestsViewModel: EmploymentRequestsViewModel = hiltViewModel()

                EmploymentAcceptTermsScreen(
                    employmentRequestsViewModel = employmentRequestsViewModel,
                    globalViewModel = employmentRequestViewModel,
                    localViewModel = employmentAcceptTermsViewModel,
                    onSubmit = {
                        val request = employmentRequestViewModel.buildEmploymentRequest()
                        employmentRequestsViewModel.createEmploymentRequest(request.toDto())

                        navController.popBackStack(MainRoute.EmploymentsRequests.route, inclusive = false)
                    },
                    onBack = { navController.popBackStack() }
                )
            }
        }

        composable(MainRoute.MyCalendar.route) { backStackEntry ->
            val viewModel = hiltViewModel<MyCalendarViewModel>(backStackEntry)

            MyCalendarScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(MainRoute.MyCurrencies.route) { backStackEntry ->
            val viewModel = hiltViewModel<MyCurrenciesViewModel>(backStackEntry)
            val coroutineScope = rememberCoroutineScope()

            MyCurrenciesScreen(
                viewModel = viewModel,
                buttonTitle = stringResource(R.string.save),
                onBack = { navController.popBackStack() },
                onNextOrSave = {
                    coroutineScope.launch {
                        viewModel.updateBusinessServices()
                    }
                },
            )
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

                val viewModel = hiltViewModel<MyProductsViewModel>(parentEntry)

                MyProductsScreen(
                    viewModel=viewModel,
                    onBack = { navController.popBackStack() },
                    onNavigateToEdit = { navController.navigate("${MainRoute.EditProduct.route}/$it") },
                    onAddProduct = { navController.navigate(MainRoute.AddProduct.route) }
                )
            }

            composable(
                route = MainRoute.AddProduct.route
            ) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(MainRoute.MyProductsNavigator.route)
                }

                val viewModel = hiltViewModel<MyProductsViewModel>(parentEntry)

                AddProductScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                )
            }

            composable(
                route = "${MainRoute.EditProduct.route}/{productId}",
                arguments = listOf(navArgument("productId") { type = NavType.IntType })
            ) { backStackEntry ->
                val productId = backStackEntry.arguments?.getInt("productId") ?:
                    throw IllegalStateException("Product Id not found in route arguments")

                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(MainRoute.MyProductsNavigator.route)
                }

                val viewModel = hiltViewModel<MyProductsViewModel>(parentEntry)

                EditProductScreen(
                    viewModel = viewModel,
                    productId = productId,
                    onBack = { navController.popBackStack() }
                )
            }
        }

        composable(
            MainRoute.Schedules.route,
        ) { backStackEntry ->
            val viewModel = hiltViewModel<MySchedulesViewModel>(backStackEntry)
            val coroutineScope = rememberCoroutineScope()

            SchedulesScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onNextOrSave = {
                    coroutineScope.launch {
                        viewModel.updateSchedules()
                    }
                }
            )
        }

    }
}