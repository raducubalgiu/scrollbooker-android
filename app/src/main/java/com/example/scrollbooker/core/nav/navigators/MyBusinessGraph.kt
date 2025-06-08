package com.example.scrollbooker.core.nav.navigators

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.scrollbooker.core.nav.routes.MainRoute
import com.example.scrollbooker.core.nav.transitions.slideInFromLeft
import com.example.scrollbooker.core.nav.transitions.slideInFromRight
import com.example.scrollbooker.core.nav.transitions.slideOutToLeft
import com.example.scrollbooker.core.nav.transitions.slideOutToRight
import com.example.scrollbooker.feature.myBusiness.calendar.presentation.MyCalendarScreen
import com.example.scrollbooker.feature.myBusiness.calendar.presentation.MyCalendarViewModel
import com.example.scrollbooker.feature.myBusiness.MyBusinessScreen
import com.example.scrollbooker.feature.products.presentation.AddProductScreen
import com.example.scrollbooker.feature.products.presentation.ProductsScreen
import com.example.scrollbooker.feature.myBusiness.schedules.presentation.SchedulesScreen
import com.example.scrollbooker.feature.myBusiness.schedules.presentation.SchedulesViewModel
import com.example.scrollbooker.feature.myBusiness.services.presentation.AttachServicesScreen
import com.example.scrollbooker.feature.myBusiness.services.presentation.MyServicesScreen
import com.example.scrollbooker.feature.myBusiness.services.presentation.ServicesViewModel
import com.example.scrollbooker.feature.products.presentation.EditProductScreen
import com.example.scrollbooker.feature.products.presentation.ProductsViewModel

fun NavGraphBuilder.myBusinessGraph(navController: NavHostController) {
    navigation(
        route = MainRoute.MyBusinessNavigator.route,
        startDestination = MainRoute.MyBusiness.route,
    ) {
        composable(
            MainRoute.MyCalendar.route,
            enterTransition = slideInFromRight(),
            exitTransition = slideOutToLeft(),
            popEnterTransition = slideInFromLeft(),
            popExitTransition = slideOutToRight()
        ) { backStackEntry ->
            val viewModel = hiltViewModel<MyCalendarViewModel>(backStackEntry)

            MyCalendarScreen(
                viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            MainRoute.MyBusiness.route,
            enterTransition = slideInFromRight(),
            exitTransition = slideOutToLeft(),
            popEnterTransition = slideInFromLeft(),
            popExitTransition = slideOutToRight()
        ) { MyBusinessScreen(
            onNavigation = { navController.navigate(it) },
            onBack = { navController.popBackStack() }
        ) }

        composable(
            MainRoute.MyServices.route,
            enterTransition = slideInFromRight(),
            exitTransition = slideOutToLeft(),
            popEnterTransition = slideInFromLeft(),
            popExitTransition = slideOutToRight()
        ) { backStackEntry ->
            val viewModel = hiltViewModel<ServicesViewModel>(backStackEntry)

            MyServicesScreen(
                viewModel,
                onBack = { navController.popBackStack() },
                onNavigate = { navController.navigate(it) }
            )
        }

        composable(
            MainRoute.AttachServices.route,
            enterTransition = slideInFromRight(),
            exitTransition = slideOutToLeft(),
            popEnterTransition = slideInFromLeft(),
            popExitTransition = slideOutToRight()
        ) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(MainRoute.MyServices.route)
            }
            val viewModel = hiltViewModel<ServicesViewModel>(parentEntry)

            AttachServicesScreen(viewModel, onBack = { navController.popBackStack() })
        }

        composable(
            MainRoute.Products.route,
            enterTransition = slideInFromRight(),
            exitTransition = slideOutToLeft(),
            popEnterTransition = slideInFromLeft(),
            popExitTransition = slideOutToRight()
        ) { backStackEntry ->
            val viewModel = hiltViewModel<ProductsViewModel>(backStackEntry)

            ProductsScreen(
                viewModel=viewModel,
                userId = 3,
                onNavigate = { navController.navigate(it) },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = "${MainRoute.EditProduct.route}/{productId}/{productName}",
            arguments = listOf(
                navArgument("productId") { type = NavType.IntType },
                navArgument("productName") { type = NavType.StringType }
            ),
            enterTransition = slideInFromRight(),
            exitTransition = slideOutToLeft(),
            popEnterTransition = slideInFromLeft(),
            popExitTransition = slideOutToRight()
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

        composable(
            MainRoute.AddProduct.route,
            enterTransition = slideInFromRight(),
            exitTransition = slideOutToLeft(),
            popEnterTransition = slideInFromLeft(),
            popExitTransition = slideOutToRight()
        ) { AddProductScreen(navController = navController) }

        composable(
            MainRoute.Schedules.route,
            enterTransition = slideInFromRight(),
            exitTransition = slideOutToLeft(),
            popEnterTransition = slideInFromLeft(),
            popExitTransition = slideOutToRight()
        ) { backStackEntry ->
            val viewModel = hiltViewModel<SchedulesViewModel>(backStackEntry)

            SchedulesScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

    }
}