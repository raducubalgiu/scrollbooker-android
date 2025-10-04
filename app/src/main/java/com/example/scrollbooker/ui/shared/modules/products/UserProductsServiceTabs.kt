package com.example.scrollbooker.ui.shared.modules.products

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.EmptyScreen
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.navigation.navigators.NavigateCalendarParam
import com.example.scrollbooker.ui.shared.userProducts.components.ServiceTab
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import kotlinx.coroutines.launch

@Composable
fun UserProductsServiceTabs(
    paddingTop: Dp,
    userId: Int,
    onNavigateToCalendar: (NavigateCalendarParam) -> Unit
) {
    val viewModel: UserProductsViewModel = hiltViewModel()
    val servicesState by viewModel.servicesState.collectAsState()

    LaunchedEffect(userId) {
        viewModel.setUserId(userId)
    }

    when(servicesState) {
        is FeatureState.Loading -> {
            LoadingScreen(
                arrangement = Arrangement.Top,
                modifier = Modifier.padding(top = 50.dp)
            )
        }
        is FeatureState.Error -> ErrorScreen()
        is FeatureState.Success -> {
            val services = (servicesState as FeatureState.Success).data

            val pagerState = rememberPagerState(initialPage = 0) { services.size }
            val coroutineScope = rememberCoroutineScope()
            val selectedTabIndex = pagerState.currentPage

            Column(modifier = Modifier.fillMaxSize()) {
                if(services.isEmpty()) {
                    EmptyScreen(
                        modifier = Modifier.padding(top = 30.dp),
                        arrangement = Arrangement.Top,
                        message = stringResource(R.string.noServicesFound),
                        icon = painterResource(R.drawable.ic_shopping_outline)
                    )
                }

                ScrollableTabRow(
                    modifier = Modifier.padding(top = paddingTop),
                    containerColor = Background,
                    contentColor = OnSurfaceBG,
                    edgePadding = BasePadding,
                    selectedTabIndex = pagerState.currentPage,
                    indicator = {},
                    divider = {
                        HorizontalDivider(
                            modifier = Modifier.padding(top = 5.dp),
                            color = Divider,
                            thickness = 0.55.dp
                        )
                    }
                ) {
                    services.forEachIndexed { index, serv ->
                        val isSelected = selectedTabIndex == index

                        ServiceTab(
                            isSelected = isSelected,
                            serviceName = serv.service.name,
                            productsCount = serv.productsCount,
                            onClick = {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            }
                        )
                    }
                }

                HorizontalPager(
                    state = pagerState,
                    beyondViewportPageCount = 0,
                    modifier = Modifier.fillMaxSize(),
                    pageSize = PageSize.Fill,
                    key = { it }
                ) { page ->
                    val serviceId = services[page].service.id
                    val employees = services[page].employees

                    UserProductsServiceTab(
                        viewModel = viewModel,
                        employees = employees,
                        userId = userId,
                        serviceId = serviceId,
                        onNavigateToCalendar = onNavigateToCalendar
                    )
                }
            }
        }
    }
}