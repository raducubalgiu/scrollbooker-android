package com.example.scrollbooker.navigation.host
import BottomBar
import android.annotation.SuppressLint
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.navigation.bottomBar.MainTab
import com.example.scrollbooker.ui.auth.AuthViewModel
import com.example.scrollbooker.ui.feed.FeedScreenViewModel
import com.example.scrollbooker.ui.main.MainDrawer
import com.example.scrollbooker.ui.main.MainUIViewModel
import com.example.scrollbooker.ui.profile.myProfile.MyProfileViewModel
import kotlinx.coroutines.launch
import timber.log.Timber

val MainTabSaver: Saver<MainTab, String> = Saver(
    save = { it.route },
    restore = { route -> MainTab.fromRoute(route) }
)

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(UnstableApi::class)
@Composable
fun MainNavHost(
    authViewModel: AuthViewModel,
    rootNavController: NavHostController
) {
    val feedViewModel: FeedScreenViewModel = hiltViewModel()
    val myProfileViewModel: MyProfileViewModel = hiltViewModel()

    val mainViewModel: MainUIViewModel = hiltViewModel()
    val myProfileData by myProfileViewModel.userProfileState.collectAsState()
    val myPosts = myProfileViewModel.userPosts.collectAsLazyPagingItems()

    val bookNowPosts = mainViewModel.bookNowPosts.collectAsLazyPagingItems()

    val businessTypesState by mainViewModel.businessTypesState.collectAsState()
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
                                businessDomainsState = businessDomainsState,
                                onClose = { scope.launch { drawerState.close() } }
                            )
                        },
                        scrimColor = Color(0xFF121212).copy(0.7f),
                        drawerState = drawerState,
                        gesturesEnabled = drawerState.currentValue == DrawerValue.Open,
                    ) {
                        FeedNavHost(
                            feedViewModel = feedViewModel,
                            mainViewModel = mainViewModel,
                            bookNowPosts = bookNowPosts,
                            rootNavController = rootNavController,
                            navController = navControllers[MainTab.Feed]!!,
                            onOpenDrawer = { scope.launch { drawerState.open() } },
                            drawerState = drawerState,
                            onChangeTab = { currentTab = it }
                        )
                    }
                }

                is MainTab.Inbox -> {
                    InboxNavHost(
                        navController = navControllers[MainTab.Inbox]!!,
                        appointmentsNumber = mainViewModel.appointmentsState,
                        onChangeTab = { currentTab = it }
                    )
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
                    ) {
                        SearchNavHost(
                            businessTypesState = businessTypesState,
                            navController = navControllers[MainTab.Search]!!
                        )
                    }
                }

                is MainTab.Appointments -> {
                    AppointmentsNavHost(
                        navController = navControllers[MainTab.Appointments]!!,
                        mainViewModel = mainViewModel,
                        appointmentsNumber = mainViewModel.appointmentsState,
                        onChangeTab = { currentTab = it }
                    )
                }
                is MainTab.Profile -> {
                    MyProfileNavHost(
                        viewModel = myProfileViewModel,
                        authViewModel = authViewModel,
                        myProfileData = myProfileData,
                        myPosts = myPosts,
                        navController = navControllers[MainTab.Profile]!!,
                        appointmentsNumber = mainViewModel.appointmentsState,
                        onChangeTab = { currentTab = it }
                    )
                }
            }
        }
    }
}
