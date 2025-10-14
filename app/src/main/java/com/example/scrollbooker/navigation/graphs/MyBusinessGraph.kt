package com.example.scrollbooker.navigation.graphs
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.scrollbooker.R
import com.example.scrollbooker.navigation.navigators.MyBusinessNavigator
import com.example.scrollbooker.navigation.routes.MainRoute
import com.example.scrollbooker.screens.auth.collectBusinessDetails.collectBusinessServices.MyServicesScreen
import com.example.scrollbooker.ui.myBusiness.MyBusinessScreen
import com.example.scrollbooker.ui.myBusiness.MyBusinessViewModel
import com.example.scrollbooker.ui.myBusiness.myCalendar.MyCalendarScreen
import com.example.scrollbooker.ui.myBusiness.myCalendar.MyCalendarViewModel
import com.example.scrollbooker.ui.myBusiness.myCurrencies.MyCurrenciesScreen
import com.example.scrollbooker.ui.myBusiness.myCurrencies.MyCurrenciesViewModel
import com.example.scrollbooker.ui.myBusiness.myEmployees.EmployeesDismissalScreen
import com.example.scrollbooker.ui.myBusiness.myEmployees.EmployeesScreen
import com.example.scrollbooker.ui.myBusiness.myEmployees.EmployeesViewModel
import com.example.scrollbooker.ui.myBusiness.myEmploymentRequests.EmploymentAcceptTermsScreen
import com.example.scrollbooker.ui.myBusiness.myEmploymentRequests.EmploymentAssignJobScreen
import com.example.scrollbooker.ui.myBusiness.myEmploymentRequests.EmploymentRequestsScreen
import com.example.scrollbooker.ui.myBusiness.myEmploymentRequests.EmploymentRequestsViewModel
import com.example.scrollbooker.ui.myBusiness.myEmploymentRequests.EmploymentSelectEmployeeScreen
import com.example.scrollbooker.ui.myBusiness.myProducts.AddProductScreen
import com.example.scrollbooker.ui.myBusiness.myProducts.EditProductScreen
import com.example.scrollbooker.ui.myBusiness.myProducts.MyProductsScreen
import com.example.scrollbooker.ui.myBusiness.myProducts.MyProductsViewModel
import com.example.scrollbooker.ui.myBusiness.mySchedules.MySchedulesScreen
import com.example.scrollbooker.ui.myBusiness.mySchedules.MySchedulesViewModel
import com.example.scrollbooker.ui.myBusiness.myServices.MyServicesViewModel
import kotlinx.coroutines.launch

fun NavGraphBuilder.myBusinessGraph(
    navController: NavHostController
) {
    navigation(
        route = MainRoute.MyBusinessNavigator.route,
        startDestination = MainRoute.MyBusiness.route
    ) {
        composable(MainRoute.MyBusiness.route) {
            val viewModel = hiltViewModel<MyBusinessViewModel>()

            val myBusinessNavigate = remember(navController) {
                MyBusinessNavigator(
                    navController = navController
                )
            }

            MyBusinessScreen(
                viewModel = viewModel,
                myBusinessNavigate = myBusinessNavigate,
                onBack = { navController.popBackStack() }
            )
        }

        composable(MainRoute.MyEmployees.route) { backStackEntry ->
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
            //val viewModel = hiltViewModel<EmployeesDismissalViewModel>(backStackEntry)

            EmployeesDismissalScreen(
                //viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        navigation(
            route = MainRoute.EmploymentRequestsNavigator.route,
            startDestination = MainRoute.EmploymentsRequests.route
        ) {
            composable(MainRoute.EmploymentsRequests.route) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(MainRoute.EmploymentRequestsNavigator.route)
                }

                val viewModel = hiltViewModel<EmploymentRequestsViewModel>(parentEntry)

                EmploymentRequestsScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                    onNavigateSelectEmployee = {
                        navController.navigate(MainRoute.EmploymentSelectEmployee.route)
                    }
                )
            }

            composable(MainRoute.EmploymentSelectEmployee.route) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(MainRoute.EmploymentRequestsNavigator.route)
                }

                val viewModel = hiltViewModel<EmploymentRequestsViewModel>(parentEntry)

                EmploymentSelectEmployeeScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                    onNext = { navController.navigate(MainRoute.EmploymentAssignJob.route) }
                )
            }

            composable(MainRoute.EmploymentAssignJob.route) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(MainRoute.EmploymentRequestsNavigator.route)
                }

                val viewModel = hiltViewModel<EmploymentRequestsViewModel>(parentEntry)

                EmploymentAssignJobScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                    onNext = { navController.navigate(MainRoute.EmploymentAcceptTerms.route) }
                )
            }

            composable(MainRoute.EmploymentAcceptTerms.route) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(MainRoute.EmploymentRequestsNavigator.route)
                }

                val viewModel = hiltViewModel<EmploymentRequestsViewModel>(parentEntry)

                EmploymentAcceptTermsScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                    onNext = {

                        navController.currentBackStackEntry?.lifecycleScope?.launch {
                            val result = viewModel.createEmploymentRequest()
                            result
                                .onSuccess {
                                    navController.navigate(MainRoute.EmploymentsRequests.route) {
                                        popUpTo(MainRoute.EmploymentRequestsNavigator.route) {
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
    }
}