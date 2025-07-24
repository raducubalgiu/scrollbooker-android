package com.example.scrollbooker.navigation.host
import BottomBar
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.scrollbooker.navigation.bottomBar.MainTab
import com.example.scrollbooker.navigation.containers.DefaultTabContainer
import com.example.scrollbooker.ui.auth.AuthViewModel
import com.example.scrollbooker.ui.feed.FeedViewModel
import com.example.scrollbooker.ui.main.MainDrawer
import com.example.scrollbooker.ui.main.MainUIViewModel
import kotlinx.coroutines.launch
import timber.log.Timber

val MainTabSaver: Saver<MainTab, String> = Saver(
    save = { it.route },
    restore = { route -> MainTab.fromRoute(route) }
)

@Composable
fun MainNavHost(authViewModel: AuthViewModel) {
    val mainViewModel: MainUIViewModel = hiltViewModel()
    val feedViewModel: FeedViewModel = hiltViewModel()

    val businessTypesState by mainViewModel.businessTypesState.collectAsState()
    val businessDomainsState by mainViewModel.businessDomainsState.collectAsState()
    val myProfileData by mainViewModel.userProfileState.collectAsState()

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

    var currentTab by rememberSaveable(stateSaver = MainTabSaver) {
        mutableStateOf(MainTab.Feed)
    }

    val currentNavController = checkNotNull(navControllers[currentTab]) {
        Timber.tag("Current NavController").e("NavController for tab $currentTab is not initialized")
    }

    val currentBackStackEntry by currentNavController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    Box(modifier = Modifier.fillMaxSize()) {
        saveableStateHolder.SaveableStateProvider(currentTab.route) {
            when (currentTab) {
                is MainTab.Feed -> {
                    ModalNavigationDrawer(
                        drawerContent = {
                            MainDrawer(
                                viewModel = mainViewModel,
                                feedViewModel = feedViewModel,
                                businessDomainsState = businessDomainsState
                            )
                        },
                        scrimColor = Color(0xFF121212).copy(0.7f),
                        drawerState = drawerState,
                        gesturesEnabled = drawerState.currentValue == DrawerValue.Open,
                    ) {
                        Scaffold(
                            bottomBar = {
                                BottomBar(
                                    appointmentsNumber = mainViewModel.appointmentsState,
                                    currentTab = currentTab,
                                    currentRoute = currentRoute,
                                    onNavigate = { currentTab = it }
                                )
                            }
                        ) { innerPadding ->
                            DefaultTabContainer(
                                navController = navControllers[MainTab.Inbox]!!,
                                enablePadding = false,
                                innerPadding = innerPadding,
                                content = {
                                    FeedNavHost(
                                        feedViewModel = feedViewModel,
                                        navController = navControllers[MainTab.Feed]!!,
                                        onOpenDrawer = { scope.launch { drawerState.open() } }
                                    )
                                }
                            )
                        }
                    }
                }

                is MainTab.Inbox -> {
                    Scaffold(
                        bottomBar = {
                            BottomBar(
                                appointmentsNumber = mainViewModel.appointmentsState,
                                currentTab = currentTab,
                                currentRoute = currentRoute,
                                onNavigate = { currentTab = it }
                            )
                        }
                    ) { innerPadding ->
                        Box(Modifier.fillMaxSize().padding(innerPadding)) {
                            InboxNavHost(navController = navControllers[MainTab.Inbox]!!)
                        }
                    }
                }

                is MainTab.Search -> {
                    Scaffold(
                        bottomBar = {
                            BottomBar(
                                appointmentsNumber = mainViewModel.appointmentsState,
                                currentTab = currentTab,
                                currentRoute = currentRoute,
                                onNavigate = { currentTab = it }
                            )
                        }
                    ) { innerPadding ->
                        DefaultTabContainer(
                            navController = navControllers[MainTab.Inbox]!!,
                            enablePadding = false,
                            innerPadding = innerPadding,
                            content = {
                                SearchNavHost(
                                    businessTypesState = businessTypesState,
                                    navController = navControllers[MainTab.Search]!!
                                )
                            }
                        )
                    }
                }

                is MainTab.Appointments -> {
                    Scaffold(
                        bottomBar = {
                            BottomBar(
                                appointmentsNumber = mainViewModel.appointmentsState,
                                currentTab = currentTab,
                                currentRoute = currentRoute,
                                onNavigate = { currentTab = it }
                            )
                        }
                    ) { innerPadding ->
                        Box(Modifier.fillMaxSize().padding(innerPadding)) {
                            AppointmentsNavHost(
                                navController = navControllers[MainTab.Appointments]!!,
                                mainViewModel = mainViewModel
                            )
                        }
                    }
                }
                is MainTab.Profile -> {
                    Scaffold(
                        bottomBar = {
                            BottomBar(
                                appointmentsNumber = mainViewModel.appointmentsState,
                                currentTab = currentTab,
                                currentRoute = currentRoute,
                                onNavigate = { currentTab = it }
                            )
                        }
                    ) { innerPadding ->
                        DefaultTabContainer(
                            enablePadding = false,
                            navController = navControllers[MainTab.Profile]!!,
                            innerPadding = innerPadding,
                            content = {
                                ProfileNavHost(
                                    myProfileData = myProfileData,
                                    navController = it,
                                    authViewModel = authViewModel
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}
