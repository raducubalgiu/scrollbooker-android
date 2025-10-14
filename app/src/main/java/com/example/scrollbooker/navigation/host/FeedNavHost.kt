package com.example.scrollbooker.navigation.host
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.scrollbooker.navigation.routes.MainRoute
import com.example.scrollbooker.ui.feed.FeedScreen
import com.example.scrollbooker.ui.feed.searchResults.FeedSearchResultsScreen
import com.example.scrollbooker.ui.feed.search.FeedSearchScreen
import com.example.scrollbooker.ui.feed.search.FeedSearchViewModel
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.search.domain.model.UserSearch
import com.example.scrollbooker.navigation.LocalRootNavController
import com.example.scrollbooker.navigation.navigators.FeedNavigator
import com.example.scrollbooker.navigation.transition.slideInFromLeft
import com.example.scrollbooker.navigation.transition.slideInFromRight
import com.example.scrollbooker.navigation.transition.slideOutToLeft
import com.example.scrollbooker.navigation.transition.slideOutToRight

@Composable
fun FeedNavHost(
    navController: NavHostController,
    feedSearchViewModel: FeedSearchViewModel,
    userSearch: FeatureState<UserSearch>
) {
    val rootNavController = LocalRootNavController.current
    val feedNavigate = remember(rootNavController, navController) {
        FeedNavigator(rootNavController, navController)
    }

    NavHost(
        navController = navController,
        startDestination = MainRoute.Feed.route,
        enterTransition = { slideInFromRight() },
        exitTransition = { slideOutToLeft() },
        popEnterTransition = { slideInFromLeft() },
        popExitTransition = { slideOutToRight() }
    ) {
        composable(route = MainRoute.Feed.route,
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) { backStackEntry ->
            FeedScreen(feedNavigate)
        }

        navigation(
            route = MainRoute.FeedSearchNavigator.route,
            startDestination = MainRoute.FeedSearch.route
        ) {
            composable(route = MainRoute.FeedSearch.route) {
                FeedSearchScreen(
                    viewModel = feedSearchViewModel,
                    userSearch = userSearch,
                    onBack = { navController.popBackStack() },
                    onGoToSearch = { navController.navigate(MainRoute.FeedSearchResults.route) },
                    onCreateUserSearch = { feedSearchViewModel.createSearch(keyword = it) },
                    onDeleteRecentlySearch = { feedSearchViewModel.deleteUserSearch(searchId = it) }
                )
            }

            composable(route = MainRoute.FeedSearchResults.route) {
                FeedSearchResultsScreen(
                    viewModel = feedSearchViewModel,
                    onBack = { navController.popBackStack() },
                    onNavigateUserProfile = { rootNavController.navigate("${MainRoute.UserProfile.route}/$it") }
                )
            }
        }
    }
}