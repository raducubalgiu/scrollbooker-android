package com.example.scrollbooker.navigation.host
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.scrollbooker.navigation.graphs.profileGraph
import com.example.scrollbooker.navigation.routes.MainRoute
import com.example.scrollbooker.ui.feed.FeedScreen
import com.example.scrollbooker.ui.feed.searchResults.FeedSearchResultsScreen
import com.example.scrollbooker.ui.feed.search.FeedSearchScreen
import com.example.scrollbooker.ui.feed.search.FeedSearchViewModel
import com.example.scrollbooker.navigation.navigators.FeedNavigator
import com.example.scrollbooker.navigation.transition.slideInFromLeft
import com.example.scrollbooker.navigation.transition.slideInFromRight
import com.example.scrollbooker.navigation.transition.slideOutToLeft
import com.example.scrollbooker.navigation.transition.slideOutToRight
import com.example.scrollbooker.ui.feed.FeedScreenViewModel

@Composable
fun FeedNavHost(navController: NavHostController) {
    val feedNavigate = remember(navController) { FeedNavigator(navController) }
    val feedViewModel: FeedScreenViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = MainRoute.Feed.route,
        enterTransition = { slideInFromRight() },
        exitTransition = { slideOutToLeft() },
        popEnterTransition = { slideInFromLeft() },
        popExitTransition = { slideOutToRight() }
    ) {
        composable(
            route = MainRoute.Feed.route,
        ) { backStackEntry ->
            FeedScreen(
                feedViewModel = feedViewModel,
                feedNavigate = feedNavigate
            )
        }

        navigation(
            route = MainRoute.FeedSearchNavigator.route,
            startDestination = MainRoute.FeedSearch.route,
        ) {
            composable(
                route = MainRoute.FeedSearch.route,
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None },
                popEnterTransition = { slideInFromLeft() },
                popExitTransition = { slideOutToRight() }
            ) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(MainRoute.FeedSearchNavigator.route)
                }

                val feedSearchViewModel = hiltViewModel<FeedSearchViewModel>(parentEntry)
                val userSearch by feedSearchViewModel.userSearch.collectAsState()

                FeedSearchScreen(
                    viewModel = feedSearchViewModel,
                    userSearch = userSearch,
                    onBack = { navController.popBackStack() },
                    onGoToSearch = { navController.navigate(MainRoute.FeedSearchResults.route) },
                    onCreateUserSearch = { feedSearchViewModel.createSearch(keyword = it) },
                    onDeleteRecentlySearch = { feedSearchViewModel.deleteUserSearch(searchId = it) }
                )
            }

            composable(
                route = MainRoute.FeedSearchResults.route,
                enterTransition = { EnterTransition.None },
                exitTransition = { slideOutToLeft() },
                popEnterTransition = { EnterTransition.None },
                popExitTransition = { slideOutToRight() }
            ) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(MainRoute.FeedSearchNavigator.route)
                }

                val feedSearchViewModel = hiltViewModel<FeedSearchViewModel>(parentEntry)

                FeedSearchResultsScreen(
                    viewModel = feedSearchViewModel,
                    onBack = { navController.popBackStack() },
                    onNavigateUserProfile = {
                        navController.navigate("${MainRoute.UserProfile.route}/$it")
                    }
                )
            }
        }

        profileGraph(navController)
    }
}