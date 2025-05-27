package com.example.scrollbooker.core.nav.host

import android.annotation.SuppressLint
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.scrollbooker.MainViewModel
import com.example.scrollbooker.R
import com.example.scrollbooker.components.inputs.Input
import com.example.scrollbooker.components.inputs.SearchBar
import com.example.scrollbooker.core.nav.BottomBar
import com.example.scrollbooker.core.nav.navigators.appointmentsGraph
import com.example.scrollbooker.core.nav.navigators.profileRootGraph
import com.example.scrollbooker.core.nav.routes.MainRoute
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.feature.feed.presentation.FeedScreen
import com.example.scrollbooker.feature.inbox.presentation.InboxScreen
import com.example.scrollbooker.feature.search.presentation.SearchScreen
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.SurfaceBG
import kotlinx.coroutines.launch

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun MainNavHost(viewModel: MainViewModel) {
    val bottomNavController = rememberNavController()
    val currentRoute = bottomNavController.currentBackStackEntryAsState().value?.destination?.route
    val isFeedScreen = (currentRoute == MainRoute.Feed.route)

    val containerColor = if(isFeedScreen) Color(0xFF121212) else Background

    val bottomBarRoutes = setOf(
        MainRoute.Feed.route,
        MainRoute.Inbox.route,
        MainRoute.Search.route,
        MainRoute.Appointments.route,
        MainRoute.Profile.route
    )

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var search by remember { mutableStateOf("") }

    ModalNavigationDrawer(
        drawerContent = {
            BoxWithConstraints {
                val drawerWidth = maxWidth * 0.7f

                ModalDrawerSheet(
                    modifier = Modifier.width(drawerWidth),
                    drawerContainerColor = SurfaceBG
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(BasePadding)
                    ) {
                        SearchBar(
                            value = search,
                            containerColor = Background,
                            contentColor = OnSurfaceBG,
                            onValueChange = { search = it },
                            placeholder = "Ce cauti?",
                            onSearch = {}
                        )

                        HorizontalDivider(modifier = Modifier
                            .padding(vertical = 8.dp),
                            color = Divider
                        )
                    }
                }
            }
        },
        scrimColor = Background,
        drawerState = drawerState,
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            bottomBar = {
                if(currentRoute in bottomBarRoutes) {
                    Column {
                        HorizontalDivider(color = Divider, thickness = 0.5.dp)
                        BottomBar(bottomNavController)
                    }
                }
            },
        ) { innerPadding ->
            NavHost(
                modifier = Modifier.padding(innerPadding),
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
                            scope.launch {
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
                    InboxScreen(
                        navController = bottomNavController
                    )
                }
                composable(MainRoute.Search.route) {
                    SearchScreen()
                }
                appointmentsGraph(navController = bottomNavController)
                profileRootGraph(navController = bottomNavController)
            }
        }
    }
}
