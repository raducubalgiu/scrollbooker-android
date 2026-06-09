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
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.example.scrollbooker.R
import com.example.scrollbooker.navigation.navigators.MyBusinessNavigator
import com.example.scrollbooker.navigation.routes.MainRoute
import com.example.scrollbooker.screens.auth.collectBusinessDetails.collectBusinessServices.MyServicesScreen
import com.example.scrollbooker.ui.LocalUserPermissions
import com.example.scrollbooker.ui.myBusiness.MyBusinessScreen
import com.example.scrollbooker.ui.myBusiness.MyBusinessViewModel
import com.example.scrollbooker.ui.myBusiness.myBusinessDetails.MyBusinessEditGalleryScreen
import com.example.scrollbooker.ui.myBusiness.myBusinessDetails.MyBusinessLocationScreen
import com.example.scrollbooker.ui.myBusiness.myBusinessDetails.MyBusinessLocationViewModel
import com.example.scrollbooker.ui.myBusiness.myCalendar.MyCalendarScreen
import com.example.scrollbooker.ui.myBusiness.myCalendar.MyCalendarViewModel
import com.example.scrollbooker.ui.myBusiness.myEmployees.MyEmployeesViewModel
import com.example.scrollbooker.ui.myBusiness.myEmployees.MyEmployeesScreen
import com.example.scrollbooker.ui.myBusiness.myEmployees.tabs.employmentRequestsTab.EmploymentAcceptTermsScreen
import com.example.scrollbooker.ui.myBusiness.myEmployees.tabs.employmentRequestsTab.EmploymentAssignJobScreen
import com.example.scrollbooker.ui.myBusiness.myEmployees.tabs.employmentRequestsTab.EmploymentSelectEmployeeScreen
import com.example.scrollbooker.ui.myBusiness.myProducts.AddProductScreen
import com.example.scrollbooker.ui.myBusiness.myProducts.AddProductsViewModel
import com.example.scrollbooker.ui.myBusiness.myProducts.EditProductScreen
import com.example.scrollbooker.ui.myBusiness.myProducts.EditProductsViewModel
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
            val permissionController = LocalUserPermissions.current

            val myBusinessNavigate = remember(navController) {
                MyBusinessNavigator(
                    navController = navController
                )
            }

            val viewModel: MyBusinessViewModel = hiltViewModel()

            MyBusinessScreen(
                viewModel = viewModel,
                permissionsController = permissionController,
                myBusinessNavigate = myBusinessNavigate,
                onBack = { navController.popBackStack() }
            )
        }

        navigation(
            route = MainRoute.MyBusinessLocationNavigator.route,
            startDestination = MainRoute.MyBusinessLocation.route
        ) {
            composable(MainRoute.MyBusinessLocation.route) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(MainRoute.MyBusinessLocationNavigator.route)
                }

                val viewModel = hiltViewModel<MyBusinessLocationViewModel>(parentEntry)

                MyBusinessLocationScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                    onNavigateToEditGallery = {
                        navController.navigate(MainRoute.MyBusinessEditGallery.route)
                    }
                )
            }

            composable(MainRoute.MyBusinessEditGallery.route) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(MainRoute.MyBusinessLocationNavigator.route)
                }

                val viewModel = hiltViewModel<MyBusinessLocationViewModel>(parentEntry)

                MyBusinessEditGalleryScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                )
            }
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

                val viewModel = hiltViewModel<MyEmployeesViewModel>(parentEntry)

                MyEmployeesScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                    onNavigateToSearchUser = { navController.navigate(MainRoute.EmploymentSelectEmployee.route) }
                )
            }

            composable(MainRoute.EmploymentSelectEmployee.route) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(MainRoute.MyEmployeesNavigator.route)
                }

                val viewModel = hiltViewModel<MyEmployeesViewModel>(parentEntry)

                EmploymentSelectEmployeeScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                    onNext = { navController.navigate(MainRoute.EmploymentAssignJob.route) }
                )
            }

            composable(MainRoute.EmploymentAssignJob.route) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(MainRoute.MyEmployeesNavigator.route)
                }

                val viewModel = hiltViewModel<MyEmployeesViewModel>(parentEntry)

                EmploymentAssignJobScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                    onNext = { navController.navigate(MainRoute.EmploymentAcceptTerms.route) }
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
                                    navController.navigate("${MainRoute.MyEmployeesNavigator.route}/1") {
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

        composable(MainRoute.MyCalendar.route) { backStackEntry ->
            val viewModel = hiltViewModel<MyCalendarViewModel>(backStackEntry)

            MyCalendarScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
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

                val viewModel: MyProductsViewModel = hiltViewModel(parentEntry)

                MyProductsScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                    onNavigateEditProduct = { serviceDomainId, productId ->
                        navController.navigate("${MainRoute.EditProduct.route}/$serviceDomainId/$productId")
                    },
                    onNavigateAddProduct = {

                    },
                )
            }

            composable(
                route = "${MainRoute.AddProduct.route}/{serviceDomainId}/{serviceId}",
                arguments = listOf(
                    navArgument("serviceDomainId") { type = NavType.IntType },
                    navArgument("serviceId") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(MainRoute.MyProductsNavigator.route)
                }

                val myProductsViewModel: MyProductsViewModel = hiltViewModel(parentEntry)
                val viewModel: AddProductsViewModel = hiltViewModel()

                AddProductScreen(
                    myProductsViewModel = myProductsViewModel,
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                )
            }

            composable(
                route = "${MainRoute.EditProduct.route}/{serviceDomainId}/{productId}",
                arguments = listOf(
                    navArgument("serviceDomainId") { type = NavType.IntType },
                    navArgument("productId") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(MainRoute.MyProductsNavigator.route)
                }

                val myProductsViewModel: MyProductsViewModel = hiltViewModel(parentEntry)
                val viewModel: EditProductsViewModel = hiltViewModel()

                EditProductScreen(
                    myProductsViewModel = myProductsViewModel,
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}