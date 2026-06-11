package com.example.scrollbooker.navigation.host
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.scrollbooker.navigation.graphs.bookingGraph
import com.example.scrollbooker.navigation.graphs.socialGraph
import com.example.scrollbooker.navigation.graphs.userProfileGraph
import com.example.scrollbooker.navigation.navigators.ProfileNavigator
import com.example.scrollbooker.navigation.navigators.SearchNavigator
import com.example.scrollbooker.navigation.routes.MainRoute
import com.example.scrollbooker.navigation.transition.slideInFromLeft
import com.example.scrollbooker.navigation.transition.slideInFromRight
import com.example.scrollbooker.navigation.transition.slideOutToLeft
import com.example.scrollbooker.navigation.transition.slideOutToRight
import com.example.scrollbooker.ui.search.businessProfile.BusinessProfileScreen
import com.example.scrollbooker.ui.search.businessProfile.BusinessProfileViewModel

@Composable
fun SearchNavHost(
    navController: NavHostController
) {
    val profileNavigate = remember(navController) {
        ProfileNavigator(navController)
    }

    val searchNavigate = remember(navController) {
        SearchNavigator(navController)
    }

    NavHost(
        navController = navController,
        startDestination = MainRoute.Search.route,
        enterTransition = { slideInFromRight() },
        exitTransition = { slideOutToLeft() },
        popEnterTransition = { slideInFromLeft() },
        popExitTransition = { slideOutToRight() }
    ) {
        composable(MainRoute.Search.route) {
            Box(Modifier.fillMaxSize())
        }
        composable(
            route = MainRoute.BusinessProfile.route,
            arguments = listOf(
                navArgument(MainRoute.BusinessProfile.ARG_BUSINESS_OWNER_USERNAME) {
                    type = NavType.StringType
                }
            ),
            enterTransition = {
                slideInFromRight()
            },
            exitTransition = {
                val targetRoute = targetState.destination.route ?: ""
                val isGoingToBooking = targetRoute.startsWith("bookingNavigator") ||
                        targetRoute.startsWith("bookingServices")

                if (isGoingToBooking) {
                    ExitTransition.None
                } else {
                    slideOutToLeft()
                }
            },
            popEnterTransition = {
                val initialRoute = initialState.destination.route ?: ""
                val isComingFromBooking = initialRoute.startsWith("bookingNavigator") ||
                        initialRoute.startsWith("bookingServices") ||
                        initialRoute.startsWith("bookingSpecialists") ||
                        initialRoute.startsWith("bookingDateTime") ||
                        initialRoute.startsWith("bookingConfirmation")

                if (isComingFromBooking) {
                    EnterTransition.None
                } else {
                    slideInFromLeft()
                }
            }
        ) {
            val viewModel: BusinessProfileViewModel = hiltViewModel()

            BusinessProfileScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                searchNavigate = searchNavigate
            )
        }

        userProfileGraph(navController, profileNavigate)
        bookingGraph(navController)
        socialGraph(navController, profileNavigate)
    }
}