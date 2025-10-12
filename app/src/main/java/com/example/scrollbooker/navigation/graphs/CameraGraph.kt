package com.example.scrollbooker.navigation.graphs

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.scrollbooker.navigation.routes.MainRoute
import com.example.scrollbooker.navigation.transition.slideInFromLeft
import com.example.scrollbooker.navigation.transition.slideInFromRight
import com.example.scrollbooker.navigation.transition.slideOutToLeft
import com.example.scrollbooker.navigation.transition.slideOutToRight
import com.example.scrollbooker.ui.camera.CameraPreviewScreen
import com.example.scrollbooker.ui.camera.CameraScreen
import com.example.scrollbooker.ui.camera.CameraViewModel
import com.example.scrollbooker.ui.camera.CreatePostScreen

fun NavGraphBuilder.cameraGraph(navController: NavHostController) {
    navigation(
        route = MainRoute.CalendarNavigator.route,
        startDestination = MainRoute.Camera.route,
        enterTransition = {
            slideInVertically(
                animationSpec = tween(240, easing = LinearOutSlowInEasing),
                initialOffsetY = { full -> full }
            ) + fadeIn(animationSpec = tween(150))
        },
        exitTransition = {
            slideOutVertically(
                animationSpec = tween(180, easing = FastOutLinearInEasing),
                targetOffsetY = { full -> full / 8 }
            ) + fadeOut(animationSpec = tween(150))
        },
        popEnterTransition = {
            slideInVertically(
                animationSpec = tween(200, easing = LinearOutSlowInEasing),
                initialOffsetY = { full -> full / 8 }
            ) + fadeIn(animationSpec = tween(150))
        },
        popExitTransition = {
            slideOutVertically(
                animationSpec = tween(260, easing = FastOutLinearInEasing),
                targetOffsetY = { full -> full }
            ) + fadeOut(animationSpec = tween(150))
        }
    ) {
        composable(
            route = MainRoute.Camera.route,
        ) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(MainRoute.CalendarNavigator.route)
            }
            val viewModel = hiltViewModel<CameraViewModel>(parentEntry)

            CameraScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onNavigateToCameraPreview = {
                    navController.navigate(MainRoute.CameraPreview.route)
                },
            )
        }

        composable(
            route = MainRoute.CameraPreview.route,
        ) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(MainRoute.CalendarNavigator.route)
            }
            val viewModel = hiltViewModel<CameraViewModel>(parentEntry)

            CameraPreviewScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(route = MainRoute.CreatePost.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(MainRoute.CalendarNavigator.route)
            }
            val viewModel = hiltViewModel<CameraViewModel>(parentEntry)

            CreatePostScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}