package com.example.scrollbooker.core.nav.host

import android.annotation.SuppressLint
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.scrollbooker.core.nav.BottomBar
import com.example.scrollbooker.core.nav.navigators.appointmentsGraph
import com.example.scrollbooker.core.nav.navigators.profileRootGraph
import com.example.scrollbooker.core.nav.routes.MainRoute
import com.example.scrollbooker.core.snackbar.SnackbarManager
import com.example.scrollbooker.feature.feed.presentation.FeedScreen
import com.example.scrollbooker.feature.inbox.presentation.InboxScreen
import com.example.scrollbooker.feature.search.presentation.SearchScreen
import com.example.scrollbooker.ui.theme.Background
import kotlinx.coroutines.launch

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun MainNavHost() {
    val bottomNavController = rememberNavController()
    val currentRoute = bottomNavController.currentBackStackEntryAsState().value?.destination?.route

    val bottomBarRoutes = setOf(
        MainRoute.Feed.route,
        MainRoute.Inbox.route,
        MainRoute.Search.route,
        MainRoute.Appointments.route,
        MainRoute.Profile.route
    )

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        SnackbarManager.messages.collect { message ->
            coroutineScope.launch {
                snackbarHostState.showSnackbar(
                    message = message.message,
                    actionLabel = message.actionLabel,
                    duration = message.duration
                )
            }
        }
    }

    ModalNavigationDrawer(
        drawerContent = { AppDrawer() },
        scrimColor = Background.copy(0.7f),
        drawerState = drawerState,
        gesturesEnabled = drawerState.currentValue == DrawerValue.Open
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            bottomBar = {
                if(currentRoute in bottomBarRoutes) {
                    Column { BottomBar(bottomNavController) }
                }
            },
            contentWindowInsets = WindowInsets(0, 0, 0, 0)
        ) { innerPadding ->
            NavHost(
                modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding()),
                navController = bottomNavController,
                startDestination = MainRoute.Feed.route,
                enterTransition = { fadeIn(animationSpec = tween(durationMillis = 500)) },
                exitTransition = { fadeOut(animationSpec = tween(durationMillis = 500)) },
                popEnterTransition = { fadeIn(animationSpec = tween(durationMillis = 500)) },
                popExitTransition = { fadeOut(animationSpec = tween(durationMillis = 500)) },
            ) {
                composable(MainRoute.Feed.route) {
                    FeedScreen(
                        onOpenDrawer = {
                            coroutineScope.launch {
                                if (drawerState.isClosed) {
                                    drawerState.open()
                                } else {
                                    drawerState.close()
                                }
                            }
                        }
                    )
                }
                composable(MainRoute.Inbox.route) {
                    Box(Modifier.fillMaxSize().statusBarsPadding()) {
                        InboxScreen(navController = bottomNavController)
                    }
                }
                composable(MainRoute.Search.route) {
                    Box(Modifier.fillMaxSize().statusBarsPadding()) {
                        SearchScreen()
                    }
                }
                appointmentsGraph(navController = bottomNavController)
                profileRootGraph(navController = bottomNavController)
            }
        }
    }
}
