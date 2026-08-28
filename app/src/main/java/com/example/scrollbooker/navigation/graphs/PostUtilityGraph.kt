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
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.example.scrollbooker.navigation.routes.MainRoute
import com.example.scrollbooker.ui.editPost.EditPostCoverScreen
import com.example.scrollbooker.ui.editPost.EditPostScreen
import com.example.scrollbooker.ui.editPost.EditPostViewModel

fun NavGraphBuilder.postUtilityGraph(navController: NavHostController) {
    val pushSpec: FiniteAnimationSpec<IntOffset> = tween(320, easing = LinearOutSlowInEasing)
    val popSpec: FiniteAnimationSpec<IntOffset> = tween(280, easing = LinearOutSlowInEasing)
    val fadeInSpec: FiniteAnimationSpec<Float> = tween(220, easing = LinearOutSlowInEasing)
    val fadeOutSpec: FiniteAnimationSpec<Float> = tween(220, easing = LinearOutSlowInEasing)

    navigation(
        route = MainRoute.EditPostNavigator.route,
        arguments = listOf(navArgument("postId") { type = NavType.IntType }),
        startDestination = MainRoute.EditPost.route,
        enterTransition = { slideInVertically(pushSpec) { it } + fadeIn(fadeInSpec) },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { slideOutVertically(popSpec) { it } + fadeOut(fadeOutSpec) }
    ) {
        composable(
            route = MainRoute.EditPost.route
        ) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(MainRoute.EditPostNavigator.route)
            }
            val viewModel = hiltViewModel<EditPostViewModel>(parentEntry)

            EditPostScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onNavigateToCoverScreen = { navController.navigate(MainRoute.EditPostCover.route) }
            )
        }

        composable(
            route = MainRoute.EditPostCover.route,
            enterTransition = { slideInVertically(pushSpec) { it } + fadeIn(fadeInSpec) },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { slideOutVertically(popSpec) { it } + fadeOut(fadeOutSpec) }
        ) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(MainRoute.EditPostNavigator.route)
            }
            val viewModel = hiltViewModel<EditPostViewModel>(parentEntry)

            EditPostCoverScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
