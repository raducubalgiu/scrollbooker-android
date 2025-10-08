package com.example.scrollbooker.navigation.host
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.navigation.LocalTabsController
import com.example.scrollbooker.navigation.bottomBar.MainTab
import com.example.scrollbooker.ui.auth.AuthViewModel
import com.example.scrollbooker.ui.MainDrawer
import com.example.scrollbooker.ui.MainUIViewModel
import com.example.scrollbooker.ui.profile.MyProfileViewModel
import com.example.scrollbooker.ui.theme.BackgroundDark
import kotlinx.coroutines.launch

@Composable
fun MainApplication(authViewModel: AuthViewModel) {
    val tabsController = LocalTabsController.current
    val currentTab by tabsController.currentTab.collectAsState()

    // MainUIViewModel
    val mainViewModel: MainUIViewModel = hiltViewModel()
    val appointmentsNumber by mainViewModel.appointmentsState.collectAsState()
    val notificationsNumber by mainViewModel.notificationsState.collectAsState()

    val myProfileViewModel: MyProfileViewModel = hiltViewModel()
    val myProfileData by myProfileViewModel.userProfileState.collectAsState()
    val myPosts = myProfileViewModel.userPosts.collectAsLazyPagingItems()

    val bookNowPosts = mainViewModel.bookNowPosts.collectAsLazyPagingItems()
    val businessDomainsState by mainViewModel.businessDomainsState.collectAsState()

    val saveableStateHolder = rememberSaveableStateHolder()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

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
                    ModalNavigationDrawer(
                        drawerContent = {
                            MainDrawer(
                                viewModel = mainViewModel,
                                businessDomainsState = businessDomainsState,
                                onClose = { scope.launch { drawerState.close() } }
                            )
                        },
                        scrimColor = BackgroundDark.copy(0.7f),
                        drawerState = drawerState,
                        gesturesEnabled = drawerState.currentValue == DrawerValue.Open,
                    ) {
                        FeedNavHost(
                            navController = navControllers[MainTab.Feed]!!,
                            mainViewModel = mainViewModel,
                            bookNowPosts = bookNowPosts,
                            appointmentsNumber = appointmentsNumber,
                            notificationsNumber = notificationsNumber,
                            onOpenDrawer = { scope.launch { drawerState.open() } },
                            drawerState = drawerState,
                        )
                    }
                }

                is MainTab.Inbox -> {
                    InboxNavHost(
                        navController = navControllers[MainTab.Inbox]!!,
                        appointmentsNumber = appointmentsNumber,
                        notificationsNumber = notificationsNumber,
                    )
                }

                is MainTab.Search -> {
                    SearchNavHost(
                        navController = navControllers[MainTab.Search]!!,
                        appointmentsNumber = appointmentsNumber,
                        notificationsNumber = notificationsNumber
                    )
                }

                is MainTab.Appointments -> {
                    AppointmentsNavHost(
                        navController = navControllers[MainTab.Appointments]!!,
                        appointmentsNumber = appointmentsNumber,
                        notificationsNumber = notificationsNumber,
                    )
                }
                is MainTab.Profile -> {
                    MyProfileNavHost(
                        navController = navControllers[MainTab.Profile]!!,
                        viewModel = myProfileViewModel,
                        authViewModel = authViewModel,
                        myProfileData = myProfileData,
                        myPosts = myPosts,
                        appointmentsNumber = appointmentsNumber,
                        notificationsNumber = notificationsNumber,
                    )
                }
            }
        }
    }
}
