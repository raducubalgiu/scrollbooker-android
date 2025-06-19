package com.example.scrollbooker.core.nav.navigators

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.scrollbooker.core.nav.routes.MainRoute
import com.example.scrollbooker.feature.myBusiness.MyBusinessScreen
import com.example.scrollbooker.feature.myBusiness.MyBusinessViewModel
import com.example.scrollbooker.screens.profile.myBusiness.myCalendar.MyCalendarScreen
import com.example.scrollbooker.screens.profile.myBusiness.myCalendar.MyCalendarViewModel
import com.example.scrollbooker.feature.myBusiness.employeeDismissal.presentation.EmployeesDismissalScreen
import com.example.scrollbooker.feature.myBusiness.employeeDismissal.presentation.EmployeesDismissalViewModel
import com.example.scrollbooker.screens.profile.myBusiness.myEmployees.EmployeesScreen
import com.example.scrollbooker.screens.profile.myBusiness.myEmployees.EmployeesViewModel
import com.example.scrollbooker.screens.profile.myBusiness.myEmploymentRequests.list.EmploymentRequestsScreen
import com.example.scrollbooker.screens.profile.myBusiness.myEmploymentRequests.list.EmploymentRequestsViewModel
import com.example.scrollbooker.feature.myBusiness.products.presentation.AddProductScreen
import com.example.scrollbooker.feature.myBusiness.products.presentation.MyProductsScreen
import com.example.scrollbooker.screens.profile.myBusiness.myCurrencies.MyCurrenciesScreen
import com.example.scrollbooker.screens.profile.myBusiness.myCurrencies.MyCurrenciesViewModel
import com.example.scrollbooker.screens.profile.myBusiness.mySchedules.SchedulesScreen
import com.example.scrollbooker.screens.profile.myBusiness.mySchedules.SchedulesViewModel
import com.example.scrollbooker.screens.profile.myBusiness.myServices.AttachServicesScreen
import com.example.scrollbooker.screens.profile.myBusiness.myServices.MyServicesScreen
import com.example.scrollbooker.screens.profile.myBusiness.myServices.MyServicesViewModel

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
            employmentRequestNavGraph(
                navController = navController
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

            MyCurrenciesScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(MainRoute.MyServices.route) { backStackEntry ->
            val viewModel = hiltViewModel<MyServicesViewModel>(backStackEntry)

            MyServicesScreen(
                viewModel,
                onBack = { navController.popBackStack() },
                onNavigate = { navController.navigate(it) }
            )
        }

        composable(MainRoute.AttachServices.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(MainRoute.MyServices.route)
            }
            val viewModel = hiltViewModel<MyServicesViewModel>(parentEntry)

            AttachServicesScreen(viewModel, onBack = { navController.popBackStack() })
        }

        composable(MainRoute.Products.route) { backStackEntry ->
            val viewModel = hiltViewModel<MyServicesViewModel>(backStackEntry)

            MyProductsScreen(
                viewModel=viewModel,
                onNavigate = { navController.navigate(it) },
                onBack = { navController.popBackStack() }
            )
        }

//        composable(route = "${MainRoute.EditProduct.route}/{productId}/{productName}",
//            arguments = listOf(
//                navArgument("productId") { type = NavType.IntType },
//                navArgument("productName") { type = NavType.StringType }
//            )
//        ) { backStackEntry ->
//            val productId = backStackEntry.arguments?.getInt("productId") ?: return@composable
//            val productName = backStackEntry.arguments?.getString("productName") ?: return@composable
//            val viewModel = hiltViewModel<MyProductsViewModel>(backStackEntry)
//
//            EditProductScreen(
//                productId=productId,
//                productName=productName,
//                onBack = { navController.popBackStack() },
//                viewModel = viewModel
//            )
//        }

        composable(MainRoute.AddProduct.route) {
            AddProductScreen(navController = navController)
        }

        composable(
            MainRoute.Schedules.route,
        ) { backStackEntry ->
            val viewModel = hiltViewModel<SchedulesViewModel>(backStackEntry)

            SchedulesScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

    }
}