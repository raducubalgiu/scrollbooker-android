package com.example.scrollbooker.core.nav.graphs
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.scrollbooker.core.nav.routes.AppointmentsDetailRoute
import com.example.scrollbooker.core.nav.routes.GlobalRoute
import com.example.scrollbooker.core.nav.routes.MainRoute
import com.example.scrollbooker.feature.appointments.presentation.AppointmentsDetailsScreen
import com.example.scrollbooker.feature.appointments.presentation.AppointmentsScreen
import com.example.scrollbooker.feature.feed.presentation.FeedScreen
import com.example.scrollbooker.feature.inbox.presentation.InboxScreen
import com.example.scrollbooker.feature.profile.presentation.ProfileScreen
import com.example.scrollbooker.feature.search.presentation.SearchScreen

fun NavGraphBuilder.mainGraph(navController: NavController) {
    navigation(
        route = GlobalRoute.MAIN,
        startDestination = MainRoute.Feed.route,
    ) {
        composable(route = MainRoute.Feed.route) {
            FeedScreen()
        }

        composable(route = MainRoute.Inbox.route) {
            InboxScreen()
        }

        composable(route = MainRoute.Search.route) {
            SearchScreen()
        }

        composable(route = MainRoute.Appointments.route) {
            AppointmentsScreen()
        }

        composable(route = AppointmentsDetailRoute.route) {
            AppointmentsDetailsScreen()
        }

        composable(route = MainRoute.Profile.route) {
            ProfileScreen()
        }
    }
}