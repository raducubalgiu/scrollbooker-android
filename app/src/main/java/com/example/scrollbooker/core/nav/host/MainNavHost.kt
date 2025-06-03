package com.example.scrollbooker.core.nav.host

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.scrollbooker.core.nav.BottomBar
import com.example.scrollbooker.core.nav.routes.MainRoute
import com.example.scrollbooker.core.snackbar.SnackbarManager
import com.example.scrollbooker.feature.appointments.presentation.AppointmentsScreen
import com.example.scrollbooker.feature.appointments.presentation.AppointmentsViewModel
import com.example.scrollbooker.feature.feed.presentation.FeedScreen
import com.example.scrollbooker.feature.feed.presentation.FeedViewModel
import com.example.scrollbooker.feature.notifications.presentation.InboxScreen
import com.example.scrollbooker.feature.notifications.presentation.InboxViewModel
import com.example.scrollbooker.feature.notifications.presentation.NotificationsScreen
import com.example.scrollbooker.feature.notifications.presentation.NotificationsViewModel
import com.example.scrollbooker.feature.profile.presentation.ProfileScreen
import com.example.scrollbooker.feature.profile.presentation.ProfileSharedViewModel
import com.example.scrollbooker.feature.search.presentation.SearchScreen
import com.example.scrollbooker.feature.search.presentation.SearchViewModel
import kotlinx.coroutines.launch


sealed class MainTab(
    val route: String,
    val label: String,
    val iconVector: ImageVector
) {
    object Feed: MainTab(MainRoute.Feed.route, "Feed", iconVector = Icons.Default.Home)
    object Inbox: MainTab(MainRoute.Inbox.route, "Inbox", iconVector = Icons.Default.Notifications)
    object Search: MainTab(MainRoute.Search.route, "Search", iconVector = Icons.Default.Search)
    object Appointments: MainTab(MainRoute.Appointments.route, "Appointments", iconVector = Icons.Default.CalendarMonth)
    object Profile: MainTab(MainRoute.Profile.route, "Profile", iconVector = Icons.Default.Person)

    companion object {
        fun fromRoute(route: String): MainTab = when(route) {
            Feed.route -> Feed
            Inbox.route -> Inbox
            Search.route -> Search
            Appointments.route -> Appointments
            Profile.route -> Profile
            else -> Feed
        }

        val allTabs = listOf(Feed, Inbox, Search, Appointments, Profile)
    }
}

val MainTabSaver: Saver<MainTab, String> = Saver(
    save = { it.route },
    restore = { route -> MainTab.fromRoute(route) }
)

@Composable
fun MainNavHost() {
    val tabItems = MainTab.allTabs
    var currentTab by rememberSaveable(stateSaver = MainTabSaver) {
        mutableStateOf(MainTab.Feed)
    }

    val saveableStateHolder = rememberSaveableStateHolder()

    Scaffold(
        bottomBar = {
            NavigationBar {
                tabItems.forEach { tab ->
                    val selected = tab == currentTab
                    NavigationBarItem(
                        icon = { Icon(imageVector = tab.iconVector, contentDescription = null) },
                        label = { Text(tab.label) },
                        selected = selected,
                        onClick = { currentTab = tab }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            saveableStateHolder.SaveableStateProvider(currentTab.route) {
                when (currentTab) {
                    is MainTab.Feed -> {
                        val navController = rememberNavController()
                        NavHost(navController, startDestination = MainRoute.Feed.route) {
                            composable(MainRoute.Feed.route) { backStackEntry ->
                                val viewModel = hiltViewModel<FeedViewModel>(backStackEntry)
                                FeedScreen(viewModel = viewModel)
                            }
                        }
                    }

                    else -> {
                        Box(modifier = Modifier
                            .fillMaxSize()
                            .statusBarsPadding()
                            .padding(innerPadding)
                        ) {
                            when (currentTab) {
                                is MainTab.Inbox -> {
                                    val navController = rememberNavController()
                                    NavHost(navController, startDestination = MainRoute.Inbox.route) {
                                        composable(MainRoute.Inbox.route) { backStackEntry ->
                                            val viewModel = hiltViewModel<InboxViewModel>(backStackEntry)
                                            InboxScreen(viewModel = viewModel)
                                        }
                                    }
                                }
                                is MainTab.Search -> {
                                    val navController = rememberNavController()
                                    NavHost(navController, startDestination = MainRoute.Search.route) {
                                        composable(MainRoute.Search.route) { backStackEntry ->
                                            val viewModel = hiltViewModel<SearchViewModel>(backStackEntry)
                                            SearchScreen(viewModel = viewModel)
                                        }
                                    }
                                }
                                is MainTab.Appointments -> {
                                    val navController = rememberNavController()
                                    NavHost(navController, startDestination = MainRoute.Appointments.route) {
                                        composable(MainRoute.Appointments.route) { backStackEntry ->
                                            val viewModel = hiltViewModel<AppointmentsViewModel>(backStackEntry)
                                            AppointmentsScreen(viewModel = viewModel)
                                        }
                                    }
                                }
                                is MainTab.Profile -> {
                                    val navController = rememberNavController()
                                    NavHost(navController, startDestination = MainRoute.Profile.route) {
                                        composable(MainRoute.Profile.route) { backStackEntry ->
                                            val viewModel = hiltViewModel<ProfileSharedViewModel>(backStackEntry)
                                            ProfileScreen(viewModel = viewModel)
                                        }
                                    }
                                }
                                else -> {}
                            }
                        }
                    }
                }
            }
        }
    }


    //val bottomNavController = rememberNavController()
    //val currentRoute = bottomNavController.currentBackStackEntryAsState().value?.destination?.route


//    val bottomBarRoutes = setOf(
//        MainRoute.Feed.route,
//        MainRoute.Inbox.route,
//        MainRoute.Search.route,
//        MainRoute.Appointments.route,
//        MainRoute.Profile.route
//    )

//    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
//
//    val snackbarHostState = remember { SnackbarHostState() }
    //val coroutineScope = rememberCoroutineScope()
//
//    LaunchedEffect(Unit) {
//        SnackbarManager.messages.collect { message ->
//            coroutineScope.launch {
//                snackbarHostState.showSnackbar(
//                    message = message.message,
//                    actionLabel = message.actionLabel,
//                    duration = message.duration
//                )
//            }
//        }
//    }

//    Scaffold(
//        //containerColor = Color.Transparent,
////        snackbarHost = {
////            Box(Modifier.fillMaxSize().statusBarsPadding()) {
////                Box(modifier = Modifier
////                    .fillMaxWidth()
////                    .align(Alignment.TopCenter)
////                ) {
////                    SnackbarHost(hostState = snackbarHostState)
////                }
////            }
////        },
//        bottomBar = {
////                if(currentRoute in bottomBarRoutes) {
////                    Column { BottomBar(bottomNavController) }
////                }
//            BottomBar(bottomNavController)
//        },
//        contentWindowInsets = WindowInsets(0, 0, 0, 0)
//    ) { innerPadding ->
//        NavHost(
//            modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding()),
//            navController = bottomNavController,
//            startDestination = MainRoute.Feed.route,
//        ) {
//            composable(MainRoute.Feed.route) { backStackEntry ->
//                val viewModel = hiltViewModel<FeedViewModel>(backStackEntry)
//
//                FeedScreen(
//                    viewModel = viewModel,
//                    onOpenDrawer = {
//                        coroutineScope.launch {
////                            if (drawerState.isClosed) drawerState.open() else drawerState.close()
//                        }
//                    }
//                )
//            }
//            composable(MainRoute.Inbox.route) { backStackEntry ->
//                val viewModel = hiltViewModel<NotificationsViewModel>(backStackEntry)
//                NotificationsScreen(viewModel)
//            }
//            composable(MainRoute.Search.route) { backStackEntry ->
//                val viewModel = hiltViewModel<SearchViewModel>(backStackEntry)
//                SearchScreen(viewModel)
//            }
//            composable(MainRoute.Appointments.route) { backStackEntry ->
//                val viewModel = hiltViewModel<AppointmentsViewModel>(backStackEntry)
//                AppointmentsScreen(viewModel)
//            }
//            composable(MainRoute.Profile.route) { backStackEntry ->
//                val viewModel = hiltViewModel<ProfileSharedViewModel>(backStackEntry)
//                ProfileScreen(viewModel=viewModel)
//            }
//            //appointmentsGraph(navController = bottomNavController)
//            //profileRootGraph(navController = bottomNavController)
//        }
//    }

//    ModalNavigationDrawer(
//        drawerContent = { AppDrawer() },
//        scrimColor = Color(0xFF121212).copy(0.7f),
//        drawerState = drawerState,
//        gesturesEnabled = drawerState.currentValue == DrawerValue.Open
//    ) {
//        Scaffold(
//            containerColor = Color.Transparent,
//            snackbarHost = {
//                Box(Modifier.fillMaxSize().statusBarsPadding()) {
//                    Box(modifier = Modifier
//                        .fillMaxWidth()
//                        .align(Alignment.TopCenter)
//                    ) {
//                        SnackbarHost(hostState = snackbarHostState)
//                    }
//                }
//            },
//            bottomBar = {
////                if(currentRoute in bottomBarRoutes) {
////                    Column { BottomBar(bottomNavController) }
////                }
//                BottomBar(bottomNavController)
//            },
//            contentWindowInsets = WindowInsets(0, 0, 0, 0)
//        ) { innerPadding ->
//            NavHost(
//                modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding()),
//                navController = bottomNavController,
//                startDestination = MainRoute.Feed.route,
//            ) {
//                composable(MainRoute.Feed.route) {
//                    FeedScreen(
//                        onOpenDrawer = {
//                            coroutineScope.launch {
//                                if (drawerState.isClosed) {
//                                    drawerState.open()
//                                } else {
//                                    drawerState.close()
//                                }
//                            }
//                        }
//                    )
//                }
//                composable(MainRoute.Inbox.route) {
//                    InboxScreen(navController = bottomNavController)
//                }
//                composable(MainRoute.Search.route) {
//                    SearchScreen()
//                }
//                appointmentsGraph(navController = bottomNavController)
//                profileRootGraph(navController = bottomNavController)
//            }
//        }
//    }
}
