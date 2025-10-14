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
import com.example.scrollbooker.ui.feed.search.FeedSearchViewModel
import com.example.scrollbooker.ui.profile.MyProfileViewModel

@Composable
fun MainApplication(onLogout: () -> Unit) {
    val tabsController = LocalTabsController.current
    val currentTab by tabsController.currentTab.collectAsState()

    // My Profile View Model
    val myProfileViewModel: MyProfileViewModel = hiltViewModel()
    val myProfileData by myProfileViewModel.userProfileState.collectAsState()
    val myPosts = myProfileViewModel.userPosts.collectAsLazyPagingItems()

    // Feed Search View Model
    val feedSearchViewModel: FeedSearchViewModel = hiltViewModel()
    val userSearch by feedSearchViewModel.userSearch.collectAsState()

    val saveableStateHolder = rememberSaveableStateHolder()
    val navControllers = remember {
        mutableMapOf<MainTab, NavHostController>()
    }.also { controllers ->
        MainTab.allTabs.forEach { tab ->
            controllers.putIfAbsent(tab, rememberNavController())
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        saveableStateHolder.SaveableStateProvider(currentTab.route) {
            when (currentTab) {
                is MainTab.Feed -> {
                    FeedNavHost(
                        navController = navControllers[MainTab.Feed]!!,
                        feedSearchViewModel = feedSearchViewModel,
                        userSearch = userSearch
                    )
                }

                is MainTab.Inbox -> {
                    InboxNavHost(
                        navController = navControllers[MainTab.Inbox]!!,
                    )
                }

                is MainTab.Search -> {
                    SearchNavHost(
                        navController = navControllers[MainTab.Search]!!,
                    )
                }

                is MainTab.Appointments -> {
                    AppointmentsNavHost(
                        navController = navControllers[MainTab.Appointments]!!,
                    )
                }
                is MainTab.Profile -> {
                    MyProfileNavHost(
                        navController = navControllers[MainTab.Profile]!!,
                        viewModel = myProfileViewModel,
                        myProfileData = myProfileData,
                        myPosts = myPosts,
                        onLogout = onLogout
                    )
                }
            }
        }
    }
}
