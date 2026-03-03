package com.example.scrollbooker.navigation.host
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.scrollbooker.navigation.graphs.userProfileGraph
import com.example.scrollbooker.navigation.routes.MainRoute
import com.example.scrollbooker.ui.feed.FeedScreen
import com.example.scrollbooker.ui.feed.searchResults.FeedSearchResultsScreen
import com.example.scrollbooker.ui.feed.search.FeedSearchScreen
import com.example.scrollbooker.ui.feed.search.FeedSearchViewModel
import com.example.scrollbooker.navigation.navigators.FeedNavigator
import com.example.scrollbooker.navigation.navigators.NavigateSocialParam
import com.example.scrollbooker.navigation.transition.slideInFromLeft
import com.example.scrollbooker.navigation.transition.slideInFromRight
import com.example.scrollbooker.navigation.transition.slideOutToLeft
import com.example.scrollbooker.navigation.transition.slideOutToRight
import com.example.scrollbooker.ui.feed.FeedScreenViewModel
import com.example.scrollbooker.ui.social.SocialScreen
import com.example.scrollbooker.ui.social.SocialViewModel

@Composable
fun FeedNavHost(navController: NavHostController) {
    val feedNavigate = remember(navController) {
        FeedNavigator(navController)
    }
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
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
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
                    onNavigateToUserProfile = { navController.navigate("${MainRoute.UserProfile.route}/$it") },
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

        userProfileGraph(navController)

        composable(
            route = "${MainRoute.Social.route}/{tabIndex}/{userId}/{username}/{isBusinessOrEmployee}",
            arguments = listOf(
                navArgument("tabIndex") { type = NavType.IntType },
                navArgument("userId") { type = NavType.IntType },
                navArgument("username") { type = NavType.StringType },
                navArgument("isBusinessOrEmployee") { type = NavType.BoolType }
            )
        ) { backStackEntry ->
            val tabIndex = backStackEntry.arguments?.getInt("tabIndex") ?: return@composable
            val userId = backStackEntry.arguments?.getInt("userId") ?: return@composable
            val username = backStackEntry.arguments?.getString("username") ?: return@composable
            val isBusinessOrEmployee = backStackEntry.arguments?.getBoolean("isBusinessOrEmployee") ?: return@composable

            val viewModel = hiltViewModel<SocialViewModel>(backStackEntry)
            val socialParams = NavigateSocialParam(tabIndex, userId, username, isBusinessOrEmployee)

            SocialScreen(
                viewModal = viewModel,
                socialParam = socialParams,
                onBack = { navController.popBackStack() },
                onNavigateUserProfile = {
                    navController.navigate("${MainRoute.UserProfile.route}/$it") {
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}