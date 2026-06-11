package com.example.scrollbooker.navigation.host
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.scrollbooker.navigation.graphs.bookingGraph
import com.example.scrollbooker.navigation.graphs.socialGraph
import com.example.scrollbooker.navigation.graphs.userProfileGraph
import com.example.scrollbooker.navigation.routes.MainRoute
import com.example.scrollbooker.ui.feed.FeedScreen
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
        ) {
            FeedScreen(
                feedViewModel = feedViewModel,
                feedNavigate = feedNavigate
            )
        }

        composable(
            route = MainRoute.FeedSearch.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { slideInFromLeft() },
            popExitTransition = { slideOutToRight() }
        ) {
            val feedSearchViewModel = hiltViewModel<FeedSearchViewModel>()

            FeedSearchScreen(
                viewModel = feedSearchViewModel,
                onBack = { navController.popBackStack() },
                onNavigateToUserProfile = { id, username ->
                    feedNavigate.toUserProfile(id, username)
                },
            )
        }

        userProfileGraph(navController)
        bookingGraph(navController)
        socialGraph(navController)
    }
}