package com.example.scrollbooker.feature.appointments.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import kotlinx.coroutines.launch

@Composable
fun AppointmentsTabs(modifier: Modifier = Modifier) {
    val pagerState = rememberPagerState(initialPage = 0) { 2 }
    val selectedTabIndex = pagerState.currentPage
    val tabs = listOf(stringResource(id = R.string.forOthers), stringResource(id = R.string.forMe))

    val onBackground = MaterialTheme.colorScheme.onBackground
    val onSurface = MaterialTheme.colorScheme.onSurface

    Column {
        TabRow(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = onSurface,
            indicator = {  tabPositions ->
                Box(
                    Modifier
                        .tabIndicatorOffset(tabPositions[selectedTabIndex])
                        .height(1.5.dp)
                        .background(onBackground)
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
                        color = if(isSelected) onBackground else onSurface,
                        fontWeight = if(isSelected) FontWeight.Bold else FontWeight.Normal
                    )}
                )
            }
        }

        HorizontalPager(state = pagerState) { page ->
            when(page) {
                0 -> AppointmentsForOtherTab()
                1 -> AppointmentsForMeTab()
            }
        }
    }
}