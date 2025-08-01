package com.example.scrollbooker.navigation.host
import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.scrollbooker.navigation.routes.MainRoute
import com.example.scrollbooker.ui.feed.FeedScreen
import com.example.scrollbooker.ui.feed.search.FeedSearchResultsScreen
import com.example.scrollbooker.ui.feed.search.FeedSearchScreen
import com.example.scrollbooker.ui.feed.search.FeedSearchViewModel
import com.example.scrollbooker.ui.main.MainUIViewModel
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.LazyPagingItems
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.navigation.bottomBar.MainTab
import com.example.scrollbooker.navigation.transition.slideInFromLeft
import com.example.scrollbooker.navigation.transition.slideInFromRight
import com.example.scrollbooker.navigation.transition.slideOutToLeft
import com.example.scrollbooker.navigation.transition.slideOutToRight

@Composable
fun FeedNavHost(
    mainViewModel: MainUIViewModel,
    rootNavController: NavHostController,
    navController: NavHostController,
    bookNowPosts: LazyPagingItems<Post>,
    onOpenDrawer: () -> Unit,
    drawerState: DrawerState,
    onNavigate: (MainTab) -> Unit
) {
    val userSearch by mainViewModel.userSearch.collectAsState()

    NavHost(
        navController = navController,
        startDestination = MainRoute.Feed.route
    ) {
        composable(route = MainRoute.Feed.route,
            popEnterTransition = { slideInFromLeft() },
            popExitTransition = { slideOutToRight() }
        ) { backStackEntry ->
            FeedScreen(
                viewModel = mainViewModel,
                drawerState = drawerState,
                onOpenDrawer = onOpenDrawer,
                onNavigateSearch = { navController.navigate(MainRoute.FeedSearchNavigator.route) },
                onNavigate = onNavigate,
                appointmentsNumber = mainViewModel.appointmentsState
            )
        }

        navigation(
            route = MainRoute.FeedSearchNavigator.route,
            startDestination = MainRoute.FeedSearch.route
        ) {
            composable(
                route = MainRoute.FeedSearch.route,
                enterTransition = { slideInFromRight() },
                exitTransition = { slideOutToLeft() },
                popEnterTransition = { slideInFromLeft() },
                popExitTransition = { slideOutToRight() }
            ) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(MainRoute.FeedSearchNavigator.route)
                }
                val viewModel: FeedSearchViewModel = hiltViewModel(parentEntry)

                FeedSearchScreen(
                    viewModel = viewModel,
                    userSearch = userSearch,
                    onBack = { navController.popBackStack() },
                    onGoToSearch = { navController.navigate(MainRoute.FeedSearchResults.route) },
                    onCreateUserSearch = { mainViewModel.createSearch(keyword = it) },
                    onDeleteRecentlySearch = { mainViewModel.deleteUserSearch(searchId = it) }
                )
            }
            composable(
                route = MainRoute.FeedSearchResults.route,
                enterTransition = { slideInFromRight() },
                exitTransition = { slideOutToLeft() },
                popEnterTransition = { slideInFromLeft() },
                popExitTransition = { slideOutToRight() }
            ) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(MainRoute.FeedSearchNavigator.route)
                }
                val viewModel: FeedSearchViewModel = hiltViewModel(parentEntry)

                FeedSearchResultsScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                    onNavigateUserProfile = { rootNavController.navigate("${MainRoute.UserProfile.route}/$it") }
                )
            }
        }
    }
}