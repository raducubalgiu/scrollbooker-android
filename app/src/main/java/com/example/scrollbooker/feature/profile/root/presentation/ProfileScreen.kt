package com.example.scrollbooker.feature.profile.root.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.scrollbooker.R
import com.example.scrollbooker.components.BottomSheet
import com.example.scrollbooker.components.list.ItemList
import com.example.scrollbooker.core.nav.routes.MainRoute
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import kotlinx.coroutines.launch

class ProfileTab(
    val route: String,
    val icon: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    var showBottomSheet by remember { mutableStateOf(false) }
    val pagerState = rememberPagerState(initialPage = 0) { 4 }
    val selectedTabIndex = pagerState.currentPage

    val tabs = listOf(
        ProfileTab(
            route = "Posts",
            icon = R.drawable.ic_grid
        ),
        ProfileTab(
            route = "Products",
            icon = R.drawable.ic_shop
        ),
        ProfileTab(
            route = "Bookmarks",
            icon = R.drawable.ic_bookmark
        ),
        ProfileTab(
            route = "Info",
            icon = R.drawable.ic_info
        )
    )

    BottomSheet(
        onDismiss = { showBottomSheet = false },
        showBottomSheet = showBottomSheet,
        showHeader = false,
        content = {
            ItemList(
                headLine = stringResource(id = R.string.calendar),
                leftIcon = painterResource(R.drawable.ic_calendar),
                displayRightIcon = false,
                onClick = {
                    showBottomSheet = false
                    navController.navigate("calendar")
                }
            )
            ItemList(
                headLine = stringResource(id = R.string.myBusiness),
                leftIcon = painterResource(R.drawable.ic_business),
                displayRightIcon = false,
                onClick = {
                    showBottomSheet = false
                    navController.navigate("myBusiness")
                }
            )
            ItemList(
                headLine = stringResource(id = R.string.settings),
                leftIcon = painterResource(R.drawable.ic_settings),
                displayRightIcon = false,
                onClick = {
                    showBottomSheet = false
                    navController.navigate("settings")
                }
            )
        }
    )

    Column(modifier = Modifier.fillMaxSize()) {
        ProfileHeader(onOpenBottomSheet = { showBottomSheet = true })

        LazyColumn {
            item {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(onClick = { navController.navigate(MainRoute.EditProfile.route) }) {
                        Text("Edit Profile")
                    }
                }
            }

            item {
                TabRow(
                    containerColor = Background,
                    contentColor = OnSurfaceBG,
                    indicator = {  tabPositions ->
                        Box(
                            Modifier
                                .tabIndicatorOffset(tabPositions[selectedTabIndex])
                                .height(1.5.dp)
                                .background(OnBackground)
                        )
                    },
                    selectedTabIndex = selectedTabIndex
                ) {
                    val coroutineScope = rememberCoroutineScope()

                    tabs.forEachIndexed { index, item ->
                        val isSelected = selectedTabIndex == index

                        Tab(
                            selected = isSelected,
                            onClick = {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            },
                            icon = {
                                Icon(
                                    painter = painterResource(id = item.icon),
                                    contentDescription = null,
                                    tint = if(isSelected) OnBackground else Color.Gray
                                )
                            }
                        )
                    }
                }
            }

//            item {
//                HorizontalPager(
//                    state = pagerState,
//                    modifier = Modifier.fillMaxWidth().heightIn(min = 500.dp)
//                ) { page ->
//                    when(page) {
//                        0 -> AppointmentsBusinessTab()
//                        1 -> AppointmentsClientTab()
//                        2 -> Column(Modifier.fillMaxSize()) {}
//                        3 -> Column(Modifier.fillMaxSize()) {  }
//                    }
//                }
//            }
        }
    }
}