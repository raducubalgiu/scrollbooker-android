package com.example.scrollbooker.feature.appointments.presentation
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.tabs.Tabs
import com.example.scrollbooker.components.core.layout.Layout
import com.example.scrollbooker.feature.appointments.presentation.components.AppointmentsList
import kotlinx.coroutines.launch

@Composable
fun AppointmentsScreen(viewModel: AppointmentsViewModel) {
    val pagerState = rememberPagerState(initialPage = 0) { 2 }
    val selectedTabIndex = pagerState.currentPage
    val tabs = listOf(stringResource(id = R.string.asEmployee), stringResource(id = R.string.asClient))
    val coroutineScope = rememberCoroutineScope()

    Layout(
        headerTitle = stringResource(id = R.string.appointments),
        enableBack = false,
        enablePadding = false
    ) {
        Column(Modifier.fillMaxSize()) {
            Tabs(tabs, selectedTabIndex, onChangeTab = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(it)
                    }
                }
            )

            HorizontalPager(
                state = pagerState,
                beyondViewportPageCount = 0,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                when(page) {
                    0 -> {
                        val customerPagingItems = viewModel.customerAppointments.collectAsLazyPagingItems()
                        AppointmentsList(pagingItems = customerPagingItems)
                    }
                    1 -> {
                        val businessPagingItems = viewModel.businessAppointments.collectAsLazyPagingItems()
                        AppointmentsList(pagingItems = businessPagingItems)
                    }
                }
            }
        }
    }
}
