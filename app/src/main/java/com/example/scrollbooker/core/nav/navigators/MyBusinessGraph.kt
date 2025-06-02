package com.example.scrollbooker.core.nav.navigators

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.scrollbooker.core.nav.routes.MainRoute
import com.example.scrollbooker.core.nav.transitions.slideInFromLeft
import com.example.scrollbooker.core.nav.transitions.slideInFromRight
import com.example.scrollbooker.core.nav.transitions.slideOutToLeft
import com.example.scrollbooker.core.nav.transitions.slideOutToRight
import com.example.scrollbooker.feature.calendar.CalendarScreen
import com.example.scrollbooker.feature.myBusiness.presentation.MyBusinessScreen
import com.example.scrollbooker.feature.products.presentation.AddProductScreen
import com.example.scrollbooker.feature.products.presentation.ProductsScreen
import com.example.scrollbooker.feature.myBusiness.presentation.schedules.SchedulesScreen
import com.example.scrollbooker.feature.myBusiness.presentation.services.ServicesScreen

fun NavGraphBuilder.myBusinessGraph(navController: NavController) {
    navigation(
        route = MainRoute.MyBusinessNavigator.route,
        startDestination = MainRoute.MyBusiness.route,
    ) {
        composable(
            MainRoute.Calendar.route,
            enterTransition = slideInFromRight(),
            exitTransition = slideOutToLeft(),
            popEnterTransition = slideInFromLeft(),
            popExitTransition = slideOutToRight()
        ) { CalendarScreen(navController) }

        composable(
            MainRoute.MyBusiness.route,
            enterTransition = slideInFromRight(),
            exitTransition = slideOutToLeft(),
            popEnterTransition = slideInFromLeft(),
            popExitTransition = slideOutToRight()
        ) { MyBusinessScreen(navController = navController) }

        composable(
            MainRoute.Services.route,
            enterTransition = slideInFromRight(),
            exitTransition = slideOutToLeft(),
            popEnterTransition = slideInFromLeft(),
            popExitTransition = slideOutToRight()
        ) { ServicesScreen(navController = navController) }

        composable(
            MainRoute.Products.route,
            enterTransition = slideInFromRight(),
            exitTransition = slideOutToLeft(),
            popEnterTransition = slideInFromLeft(),
            popExitTransition = slideOutToRight()
        ) { ProductsScreen(navController = navController, userId = 3) }

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
        ) { SchedulesScreen(navController = navController) }
    }
}