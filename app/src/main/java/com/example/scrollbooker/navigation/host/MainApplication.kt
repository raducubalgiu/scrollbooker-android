package com.example.scrollbooker.navigation.host
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.navigation.LocalTabsController
import com.example.scrollbooker.navigation.bottomBar.MainTab
import com.example.scrollbooker.navigation.routes.MainRoute
import com.example.scrollbooker.ui.profile.MyProfileViewModel
import com.example.scrollbooker.ui.profile.components.ProfileLayoutViewModel
import com.example.scrollbooker.ui.search.SearchScreen
import com.example.scrollbooker.ui.search.SearchViewModel

@Composable
fun MainApplication(onLogout: () -> Unit) {
    val tabsController = LocalTabsController.current
    val currentTab by tabsController.currentTab.collectAsState()

    // My Profile View Model
    val myProfileViewModel: MyProfileViewModel = hiltViewModel()
    val layoutViewModel: ProfileLayoutViewModel = hiltViewModel()
    val searchViewModel: SearchViewModel = hiltViewModel()

    val myProfileData by myProfileViewModel.userProfileState.collectAsState()
    val myPosts = layoutViewModel.posts.collectAsLazyPagingItems()

    val saveableStateHolder = rememberSaveableStateHolder()
    val navControllers = remember {
        mutableMapOf<MainTab, NavHostController>()
    }.also { controllers ->
        MainTab.allTabs.forEach { tab ->
            controllers.putIfAbsent(tab, rememberNavController())
        }
    }

    val searchNavHostController = navControllers[MainTab.Search]!!

    Box(modifier = Modifier.fillMaxSize()) {
        SearchScreen(
            viewModel = searchViewModel,
            isSearchTab = currentTab is MainTab.Search,
            onNavigateToBusinessProfile = {
                searchNavHostController.navigate(MainRoute.BusinessProfile.createRoute(it))
            }
        )

        saveableStateHolder.SaveableStateProvider(currentTab.route) {
            when (currentTab) {
                is MainTab.Feed -> FeedNavHost(navController = navControllers[MainTab.Feed]!!)
                is MainTab.Inbox -> InboxNavHost(navControllers[MainTab.Inbox]!!)
                is MainTab.Search -> SearchNavHost(searchNavHostController)
                is MainTab.Appointments -> AppointmentsNavHost(navControllers[MainTab.Appointments]!!)

                is MainTab.Profile -> {
                    MyProfileNavHost(
                        navController = navControllers[MainTab.Profile]!!,
                        viewModel = myProfileViewModel,
                        layoutViewModel = layoutViewModel,
                        myProfileData = myProfileData,
                        myPosts = myPosts,
                        onLogout = onLogout
                    )
                }
            }
        }
    }
}
