package com.example.scrollbooker.feature.profile.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.scrollbooker.R
import com.example.scrollbooker.components.BottomSheet
import com.example.scrollbooker.components.ItemList
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    var showBottomSheet by remember { mutableStateOf(false) }
    val pagerState = rememberPagerState(initialPage = 0) { 2 }
    val selectedTabIndex = pagerState.currentPage
    val tabs = listOf(stringResource(id = R.string.asEmployee), stringResource(id = R.string.asClient))

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

        Column {
            TabRow(
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onSurface,
                indicator = {  tabPositions ->
                    Box(
                        Modifier
                            .tabIndicatorOffset(tabPositions[selectedTabIndex])
                            .height(1.5.dp)
                            .background(MaterialTheme.colorScheme.onBackground)
                    )
                },
                selectedTabIndex = selectedTabIndex
            ) {
                val coroutineScope = rememberCoroutineScope()

                tabs.forEachIndexed { index, title ->
                    val isSelected = selectedTabIndex == index

                    Tab(
                        selected = isSelected,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        text = { Text(
                            text = title,
                            style = MaterialTheme.typography.bodyLarge,
                            color = if(isSelected) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.onSurface,
                            fontWeight = if(isSelected) FontWeight.Bold else FontWeight.Normal
                        )}
                    )
                }
            }
        }
    }
}