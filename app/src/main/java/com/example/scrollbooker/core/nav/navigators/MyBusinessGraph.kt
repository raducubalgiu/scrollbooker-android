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
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.scrollbooker.core.nav.routes.MainRoute
import com.example.scrollbooker.core.nav.transitions.slideEnterTransition
import com.example.scrollbooker.core.nav.transitions.slideExitTransition
import com.example.scrollbooker.feature.myBusiness.calendar.presentation.MyCalendarScreen
import com.example.scrollbooker.feature.myBusiness.calendar.presentation.MyCalendarViewModel
import com.example.scrollbooker.feature.myBusiness.MyBusinessScreen
import com.example.scrollbooker.feature.myBusiness.employees.presentation.EmployeesDismissalScreen
import com.example.scrollbooker.feature.myBusiness.employees.presentation.EmployeesDismissalViewModel
import com.example.scrollbooker.feature.myBusiness.employees.presentation.EmployeesScreen
import com.example.scrollbooker.feature.myBusiness.employees.presentation.EmployeesViewModel
import com.example.scrollbooker.feature.products.presentation.AddProductScreen
import com.example.scrollbooker.feature.products.presentation.ProductsScreen
import com.example.scrollbooker.feature.myBusiness.services.presentation.AttachServicesScreen
import com.example.scrollbooker.feature.myBusiness.services.presentation.MyServicesScreen
import com.example.scrollbooker.feature.myBusiness.services.presentation.ServicesViewModel
import com.example.scrollbooker.feature.products.presentation.EditProductScreen
import com.example.scrollbooker.feature.products.presentation.ProductsViewModel
import com.example.scrollbooker.feature.schedules.presentation.SchedulesScreen
import com.example.scrollbooker.feature.schedules.presentation.SchedulesViewModel

fun NavGraphBuilder.myBusinessGraph(navController: NavHostController) {
    navigation(
        route = MainRoute.MyBusinessNavigator.route,
        startDestination = MainRoute.MyBusiness.route,
    ) {
        composable(
            MainRoute.MyBusiness.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(
                        250,
                        easing = FastOutSlowInEasing
                    )
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    tween(
                        250,
                        easing = FastOutSlowInEasing
                    )
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(
                        250,
                        easing = FastOutSlowInEasing
                    )
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    tween(
                        250,
                        easing = FastOutSlowInEasing
                    )
                )
            }
        ) { MyBusinessScreen(
            onNavigation = { navController.navigate(it) },
            onBack = { navController.popBackStack() }
        ) }

        composable(MainRoute.Employees.route,
            enterTransition = slideEnterTransition(),
            popExitTransition = slideExitTransition()
        ) { backStackEntry ->
            val viewModel = hiltViewModel<EmployeesViewModel>(backStackEntry)

            EmployeesScreen(
                viewModel,
                onBack = { navController.popBackStack() },
                onNavigate = { navController.navigate(it) }
            )
        }

        composable("${MainRoute.EmployeesDismissal.route}/{userId}",
            enterTransition = slideEnterTransition(),
            popExitTransition = slideExitTransition(),
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

        composable(MainRoute.MyCalendar.route,
            enterTransition = slideEnterTransition(),
            popExitTransition = slideExitTransition()
        ) { backStackEntry ->
            val viewModel = hiltViewModel<MyCalendarViewModel>(backStackEntry)

            MyCalendarScreen(
                viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(MainRoute.MyServices.route,
            enterTransition = slideEnterTransition(),
            popExitTransition = slideExitTransition()
        ) { backStackEntry ->
            val viewModel = hiltViewModel<ServicesViewModel>(backStackEntry)

            MyServicesScreen(
                viewModel,
                onBack = { navController.popBackStack() },
                onNavigate = { navController.navigate(it) }
            )
        }

        composable(MainRoute.AttachServices.route,
            enterTransition = slideEnterTransition(),
            popExitTransition = slideExitTransition()
        ) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(MainRoute.MyServices.route)
            }
            val viewModel = hiltViewModel<ServicesViewModel>(parentEntry)

            AttachServicesScreen(viewModel, onBack = { navController.popBackStack() })
        }

        composable(MainRoute.Products.route,
            enterTransition = slideEnterTransition(),
            popExitTransition = slideExitTransition()
        ) { backStackEntry ->
            val viewModel = hiltViewModel<ServicesViewModel>(backStackEntry)

            ProductsScreen(
                viewModel=viewModel,
                onNavigate = { navController.navigate(it) },
                onBack = { navController.popBackStack() }
            )
        }

        composable(route = "${MainRoute.EditProduct.route}/{productId}/{productName}",
            enterTransition = slideEnterTransition(),
            popExitTransition = slideExitTransition(),
            arguments = listOf(
                navArgument("productId") { type = NavType.IntType },
                navArgument("productName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getInt("productId") ?: return@composable
            val productName = backStackEntry.arguments?.getString("productName") ?: return@composable
            val viewModel = hiltViewModel<ProductsViewModel>(backStackEntry)

            EditProductScreen(
                productId=productId,
                productName=productName,
                onBack = { navController.popBackStack() },
                viewModel = viewModel
            )
        }

        composable(MainRoute.AddProduct.route,
            enterTransition = slideEnterTransition(),
            popExitTransition = slideExitTransition()
        ) { AddProductScreen(navController = navController) }

        composable(
            MainRoute.Schedules.route,
            enterTransition = slideEnterTransition(),
            popExitTransition = slideExitTransition()
        ) { backStackEntry ->
            val viewModel = hiltViewModel<SchedulesViewModel>(backStackEntry)

            SchedulesScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

    }
}