package com.example.scrollbooker.navigation.host
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.scrollbooker.navigation.graphs.appointmentsGraph
import com.example.scrollbooker.navigation.graphs.bookingGraph
import com.example.scrollbooker.navigation.graphs.postUtilityGraph
import com.example.scrollbooker.navigation.graphs.reviewsGraph
import com.example.scrollbooker.navigation.graphs.socialGraph
import com.example.scrollbooker.navigation.graphs.userProfileGraph
import com.example.scrollbooker.navigation.navigators.AppointmentsNavigator
import com.example.scrollbooker.navigation.navigators.BookingNavigator
import com.example.scrollbooker.navigation.navigators.ReviewsNavigator
import com.example.scrollbooker.navigation.routes.MainRoute
import com.example.scrollbooker.ui.feed.FeedScreen
import com.example.scrollbooker.ui.feed.search.FeedSearchScreen
import com.example.scrollbooker.ui.feed.search.FeedSearchViewModel
import com.example.scrollbooker.navigation.navigators.FeedNavigator
import com.example.scrollbooker.navigation.navigators.ProfileNavigator
import com.example.scrollbooker.navigation.transition.slideInFromLeft
import com.example.scrollbooker.navigation.transition.slideInFromRight
import com.example.scrollbooker.navigation.transition.slideOutToLeft
import com.example.scrollbooker.navigation.transition.slideOutToRight
import com.example.scrollbooker.ui.feed.ExploreFeedViewModel
import com.example.scrollbooker.ui.feed.FollowingFeedViewModel

@Composable
fun FeedNavHost(navController: NavHostController) {
    val profileNavigate = remember(navController) { ProfileNavigator(navController) }
    val feedNavigate = remember(navController) { FeedNavigator(navController) }
    val bookingNavigate = remember(navController) { BookingNavigator(navController) }
    val appointmentNavigate = remember(navController) { AppointmentsNavigator(navController) }
    val reviewsNavigate = remember(navController) { ReviewsNavigator(navController) }

    NavHost(
        navController = navController,
        startDestination = MainRoute.Feed.route,
        enterTransition = { slideInFromRight() },
        exitTransition = {
            val route = targetState.destination.route
            if (route?.startsWith(MainRoute.EditPost.route) == true) {
                ExitTransition.None
            } else {
                slideOutToLeft()
            }
        },
        popEnterTransition = {
            val route = initialState.destination.route
            if (route?.startsWith(MainRoute.EditPost.route) == true) {
                EnterTransition.None
            } else {
                slideInFromLeft()
            }
        },
        popExitTransition = { slideOutToRight() }
    ) {
        composable(route = MainRoute.Feed.route) {
            val exploreViewModel: ExploreFeedViewModel = hiltViewModel()
            val followingViewModel: FollowingFeedViewModel = hiltViewModel()

            FeedScreen(
                exploreViewModel = exploreViewModel,
                followingViewModel = followingViewModel,
                feedNavigate = feedNavigate
            )
        }

        composable(route = MainRoute.FeedSearch.route) {
            val feedSearchViewModel = hiltViewModel<FeedSearchViewModel>()

            FeedSearchScreen(
                viewModel = feedSearchViewModel,
                feedNavigate = feedNavigate
            )
        }

        appointmentsGraph(appointmentNavigate)
        userProfileGraph(navController, profileNavigate)
        bookingGraph(navController, bookingNavigate)
        socialGraph(navController, profileNavigate)
        postUtilityGraph(navController)
        reviewsGraph(navController, reviewsNavigate)
    }
}