package com.example.scrollbooker.feature.userSocial.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.Tabs
import com.example.scrollbooker.components.core.Layout
import kotlinx.coroutines.launch

@Composable
fun UserSocialScreen(
    viewModal: UserSocialViewModel,
    onBack: () -> Unit,
    initialPage: Int,
    userId: Int,
    username: String
) {
    val pagerState = rememberPagerState(initialPage = initialPage ) { 3 }
    val selectedTabIndex = pagerState.currentPage
    val tabs = listOf(
        stringResource(id = R.string.reviews),
        stringResource(id = R.string.followers),
        stringResource(id = R.string.following)
    )
    val coroutineScope = rememberCoroutineScope()

    Layout(
        headerTitle = username,
        onBack = onBack,
        enablePadding = false
    ) {

        Column {
            Tabs(tabs, selectedTabIndex, onChangeTab = {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(it)
                }
            })

            HorizontalPager(
                state = pagerState,
                beyondViewportPageCount = 0,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                when(page) {
                    0 -> {
                        //val customerPagingItems = viewModel.customerAppointments().collectAsLazyPagingItems()
                        Column(Modifier.fillMaxSize()) {
                            Text("Reviews Screen")
                        }
                    }
                    1 -> {
                        //val businessPagingItems = viewModel.businessAppointments().collectAsLazyPagingItems()
                        Column(Modifier.fillMaxSize()) {
                            Text("Followers Screen")
                        }
                    }

                    2 -> {
                        Column(Modifier.fillMaxSize()) {
                            Text("Followings Screen")
                        }
                    }
                }
            }
        }
    }
}