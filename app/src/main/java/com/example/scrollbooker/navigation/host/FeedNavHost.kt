package com.example.scrollbooker.navigation.host
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.scrollbooker.navigation.routes.MainRoute
import com.example.scrollbooker.ui.feed.FeedScreen
import com.example.scrollbooker.ui.feed.FeedViewModel
import com.example.scrollbooker.ui.feed.search.FeedSearchResultsScreen
import com.example.scrollbooker.ui.feed.search.FeedSearchScreen
import com.example.scrollbooker.ui.feed.search.FeedSearchViewModel

@Composable
fun FeedNavHost(
    feedViewModel: FeedViewModel,
    navController: NavHostController,
    onOpenDrawer: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = MainRoute.Feed.route
    ) {
        composable(route = MainRoute.Feed.route,
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
        ) { backStackEntry ->
            FeedScreen(
                viewModel = feedViewModel,
                onOpenDrawer = onOpenDrawer,
                onNavigateSearch = {
                    navController.navigate(MainRoute.FeedSearchNavigator.route)
                }
            )
        }

        navigation(
            route = MainRoute.FeedSearchNavigator.route,
            startDestination = MainRoute.FeedSearch.route
        ) {
            composable(
                route = MainRoute.FeedSearch.route,
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(
                            durationMillis = 250,
                            easing = FastOutSlowInEasing
                        )
                    )
                }
            ) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(MainRoute.FeedSearchNavigator.route)
                }
                val viewModel: FeedSearchViewModel = hiltViewModel(parentEntry)

                FeedSearchScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                    onGoToSearch = { navController.navigate(MainRoute.FeedSearchResults.route) }
                )
            }
            composable(
                route = MainRoute.FeedSearchResults.route,
                enterTransition = { fadeIn(animationSpec = tween(200)) },
                exitTransition = { fadeOut(animationSpec = tween(200)) }
            ) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(MainRoute.FeedSearchNavigator.route)
                }
                val viewModel: FeedSearchViewModel = hiltViewModel(parentEntry)

                FeedSearchResultsScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}