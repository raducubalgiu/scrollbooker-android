package com.example.scrollbooker.navigation.graphs
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.scrollbooker.navigation.LocalTabsController
import com.example.scrollbooker.navigation.TabsController
import com.example.scrollbooker.navigation.TabsViewModel
import com.example.scrollbooker.navigation.routes.MainRoute
import com.example.scrollbooker.navigation.routes.RootRoute
import com.example.scrollbooker.ui.BottomBarController
import com.example.scrollbooker.ui.LocalBottomBarController
import com.example.scrollbooker.ui.MainUIViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.navigation.bottomBar.MainTab
import com.example.scrollbooker.navigation.host.AppointmentsNavHost
import com.example.scrollbooker.navigation.host.FeedNavHost
import com.example.scrollbooker.navigation.host.InboxNavHost
import com.example.scrollbooker.navigation.host.MyProfileNavHost
import com.example.scrollbooker.navigation.host.SearchNavHost
import com.example.scrollbooker.ui.LocalLocationController
import com.example.scrollbooker.ui.LocalMainNavController
import com.example.scrollbooker.ui.LocalUserPermissions
import com.example.scrollbooker.ui.LocationController
import com.example.scrollbooker.ui.LocationViewModel
import com.example.scrollbooker.ui.UserPermissionsController
import com.example.scrollbooker.ui.profile.MyProfileViewModel
import com.example.scrollbooker.ui.profile.components.ProfileLayoutViewModel
import com.example.scrollbooker.ui.search.SearchScreen
import com.example.scrollbooker.ui.search.SearchViewModel

fun NavGraphBuilder.mainGraph(onLogout: () -> Unit) {
    navigation(
        route = RootRoute.MAIN,
        startDestination = MainRoute.Shell.route
    ) {
        composable(route = MainRoute.Shell.route) {
            val tabsViewModel: TabsViewModel = hiltViewModel()
            val mainUiViewModel: MainUIViewModel = hiltViewModel()
            val locationViewModel: LocationViewModel = hiltViewModel()

            val permissions by mainUiViewModel.permissions.collectAsStateWithLifecycle()
            val hasEmployees by mainUiViewModel.hasEmployees.collectAsStateWithLifecycle()

            val tabsController = remember(tabsViewModel) {
                TabsController(
                    currentTab = tabsViewModel.currentTab,
                    setTab = tabsViewModel::setTab
                )
            }

            val bottomBarController = remember(mainUiViewModel) {
                BottomBarController(
                    appointments = mainUiViewModel.appointments,
                    notifications = mainUiViewModel.notifications,
                    incAppointments = mainUiViewModel::incAppointmentsNumber,
                    decAppointments = mainUiViewModel::decAppointmentsNumber,
                    setAppointments = mainUiViewModel::setAppointments,
                    setNotifications = mainUiViewModel::setNotifications,
                )
            }

            val permissionsController = remember(permissions, hasEmployees) {
                UserPermissionsController(permissions, hasEmployees)
            }

            val locationController = remember(locationViewModel) {
                LocationController(
                    stateFlow = locationViewModel.state,
                    startUpdates = locationViewModel::startLocationUpdates,
                    stopUpdates = locationViewModel::stopLocationUpdates,
                    onPermissionResult = locationViewModel::onPermissionResult,
                    syncInitialPermission = locationViewModel::syncInitialPermission
                )
            }

            val mainNavController = rememberNavController()
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

            CompositionLocalProvider(
                LocalMainNavController provides mainNavController,
                LocalTabsController provides tabsController,
                LocalBottomBarController provides bottomBarController,
                LocalUserPermissions provides permissionsController,
                LocalLocationController provides locationController
            ) {
                NavHost(
                    navController = mainNavController,
                    startDestination = MainRoute.Tabs.route
                ) {
                    composable(MainRoute.Tabs.route) {
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
                                    is MainTab.Feed -> {
                                        FeedNavHost(navController = navControllers[MainTab.Feed]!!)
                                    }
                                    is MainTab.Inbox -> {
                                        InboxNavHost(navControllers[MainTab.Inbox]!!)
                                    }
                                    is MainTab.Search -> {
                                        SearchNavHost(searchNavHostController)
                                    }
                                    is MainTab.Appointments -> {
                                        AppointmentsNavHost(navControllers[MainTab.Appointments]!!)
                                    }

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
                }
            }
        }
    }
}