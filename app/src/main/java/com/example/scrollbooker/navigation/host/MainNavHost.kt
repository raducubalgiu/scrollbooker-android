package com.example.scrollbooker.navigation.host
import android.annotation.SuppressLint
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.navigation.bottomBar.MainTab
import com.example.scrollbooker.ui.auth.AuthViewModel
import com.example.scrollbooker.ui.feed.FeedScreenViewModel
import com.example.scrollbooker.ui.MainDrawer
import com.example.scrollbooker.ui.MainUIViewModel
import com.example.scrollbooker.ui.profile.MyProfileViewModel
import com.example.scrollbooker.ui.theme.BackgroundDark
import kotlinx.coroutines.launch

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
    val myProfileViewModel: MyProfileViewModel = hiltViewModel()
    val feedViewModel: FeedScreenViewModel = hiltViewModel()

    val mainViewModel: MainUIViewModel = hiltViewModel()
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

    var currentTab by rememberSaveable(stateSaver = MainTabSaver) {
        mutableStateOf(MainTab.Feed)
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
                        rootNavController = rootNavController,
                        navController = navControllers[MainTab.Inbox]!!,
                        appointmentsNumber = mainViewModel.appointmentsState,
                        onChangeTab = { currentTab = it }
                    )
                }

                is MainTab.Search -> {
                    SearchNavHost(
                        navController = navControllers[MainTab.Search]!!,
                        appointmentsNumber = mainViewModel.appointmentsState,
                        onChangeTab = { currentTab = it }
                    )
                }

                is MainTab.Appointments -> {
                    AppointmentsNavHost(
                        rootNavController = rootNavController,
                        navController = navControllers[MainTab.Appointments]!!,
                        appointmentsNumber = mainViewModel.appointmentsState,
                        onChangeTab = { currentTab = it }
                    )
                }
                is MainTab.Profile -> {
                    MyProfileNavHost(
                        rootNavController = rootNavController,
                        navController = navControllers[MainTab.Profile]!!,
                        viewModel = myProfileViewModel,
                        authViewModel = authViewModel,
                        myProfileData = myProfileData,
                        myPosts = myPosts,
                        appointmentsNumber = mainViewModel.appointmentsState,
                        onChangeTab = { currentTab = it }
                    )
                }
            }
        }
    }
}
