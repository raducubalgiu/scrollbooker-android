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
import androidx.compose.ui.unit.IntOffset
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.scrollbooker.navigation.routes.MainRoute
import com.example.scrollbooker.navigation.transition.slideInFromRight
import com.example.scrollbooker.navigation.transition.slideOutToLeft
import com.example.scrollbooker.ui.camera.CameraGalleryScreen
import com.example.scrollbooker.ui.camera.CameraPreviewScreen
import com.example.scrollbooker.ui.camera.CameraScreen
import com.example.scrollbooker.ui.camera.CameraViewModel
import com.example.scrollbooker.ui.camera.CreatePostPreviewScreen
import com.example.scrollbooker.ui.camera.CreatePostScreen

fun NavGraphBuilder.cameraGraph(mainNavController: NavHostController) {
    val pushSpec: FiniteAnimationSpec<IntOffset> = tween(320, easing = LinearOutSlowInEasing)
    val popSpec: FiniteAnimationSpec<IntOffset> = tween(280, easing = LinearOutSlowInEasing)
    val fadeInSpec: FiniteAnimationSpec<Float> = tween(220, easing = LinearOutSlowInEasing)
    val fadeOutSpec: FiniteAnimationSpec<Float> = tween(220, easing = LinearOutSlowInEasing)

    navigation(
        route = MainRoute.CameraNavigator.route,
        startDestination = MainRoute.Camera.route,
        enterTransition = { slideInVertically(pushSpec) { it } + fadeIn(fadeInSpec) },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { slideOutVertically(popSpec) { it } + fadeOut(fadeOutSpec) }
    ) {
        composable(
            route = MainRoute.Camera.route
        ) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                mainNavController.getBackStackEntry(MainRoute.CameraNavigator.route)
            }
            val viewModel = hiltViewModel<CameraViewModel>(parentEntry)

            CameraScreen(
                viewModel = viewModel,
                onBack = { mainNavController.popBackStack() },
                onNavigateToCameraGallery = {
                    mainNavController.navigate(MainRoute.CameraGallery.route)
                }
            )
        }

        composable(
            route = MainRoute.CameraGallery.route,
            enterTransition = { fadeIn() },
            popExitTransition = { ExitTransition.None }
        ) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                mainNavController.getBackStackEntry(MainRoute.CameraNavigator.route)
            }
            val viewModel = hiltViewModel<CameraViewModel>(parentEntry)

            CameraGalleryScreen(
                viewModel = viewModel,
                onBack = { mainNavController.popBackStack() },
                onNavigateToCameraPreview = {
                    mainNavController.navigate(MainRoute.CameraPreview.route)
                }
            )
        }

        composable(
            route = MainRoute.CameraPreview.route,
            popExitTransition = { slideOutVertically(popSpec) { it } }
        ) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                mainNavController.getBackStackEntry(MainRoute.CameraNavigator.route)
            }
            val viewModel = hiltViewModel<CameraViewModel>(parentEntry)

            CameraPreviewScreen(
                viewModel = viewModel,
                onBack = { mainNavController.popBackStack() },
                onNavigateToCreatePostScreen = {
                    mainNavController.navigate(MainRoute.CreatePost.route)
                }
            )
        }

        composable(
            route = MainRoute.CreatePost.route,
            enterTransition = { slideInFromRight() },
            exitTransition = { slideOutToLeft() },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                mainNavController.getBackStackEntry(MainRoute.CameraNavigator.route)
            }
            val viewModel = hiltViewModel<CameraViewModel>(parentEntry)

            CreatePostScreen(
                viewModel = viewModel,
                onBack = { mainNavController.popBackStack() },
                onNavigateToPostPreview = {
                    mainNavController.navigate(MainRoute.CreatePostPreview.route)
                }
            )
        }

        composable(
            route = MainRoute.CreatePostPreview.route,
            enterTransition = { slideInVertically(pushSpec) { it } + fadeIn(fadeInSpec) },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { slideOutVertically(popSpec) { it } + fadeOut(fadeOutSpec) }
        ) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                mainNavController.getBackStackEntry(MainRoute.CameraNavigator.route)
            }
            val viewModel = hiltViewModel<CameraViewModel>(parentEntry)

            CreatePostPreviewScreen(
                viewModel = viewModel,
                onBack = { mainNavController.popBackStack() }
            )
        }
    }
}