package com.example.scrollbooker.ui.shared.products

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.EmptyScreen
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.ui.shared.products.components.ServiceTab
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import kotlinx.coroutines.launch

@Composable
fun UserProductsServiceTabs(
    viewModel: UserProductsViewModel,
    selectedProducts: Set<Product>,
    userId: Int,
    onSelect: (Product) -> Unit
) {
    val servicesDomainWithServices by viewModel.serviceDomainWithServicesState.collectAsState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(userId) {
        viewModel.setUserId(userId)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        when(val sDomainServices = servicesDomainWithServices) {
            is FeatureState.Loading -> Unit
            is FeatureState.Error -> ErrorScreen()
            is FeatureState.Success -> {
                val data = sDomainServices.data

                if(data.size == 1) {
                    val withoutServiceDomain = sDomainServices.data.first().services

                    val pagerState = rememberPagerState(initialPage = 0) { withoutServiceDomain.size }
                    val selectedTabIndex = pagerState.currentPage

                    Column(modifier = Modifier.fillMaxSize()) {
                        if(withoutServiceDomain.isEmpty()) {
                            EmptyScreen(
                                modifier = Modifier.padding(top = 30.dp),
                                arrangement = Arrangement.Top,
                                message = stringResource(R.string.noServicesFound),
                                icon = painterResource(R.drawable.ic_shopping_outline)
                            )
                        }

                        ScrollableTabRow(
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
                            withoutServiceDomain.forEachIndexed { index, serv ->
                                val isSelected = selectedTabIndex == index

                                ServiceTab(
                                    isSelected = isSelected,
                                    serviceName = serv.service.displayName,
                                    count = serv.productsCount,
                                    onClick = {
                                        scope.launch {
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
                            val serviceId = withoutServiceDomain[page].service.id
                            val employees = withoutServiceDomain[page].employees

                            UserProductsServiceTab(
                                viewModel = viewModel,
                                selectedProducts = selectedProducts,
                                employees = employees,
                                userId = userId,
                                serviceId = serviceId,
                                onSelect = onSelect
                            )
                        }
                    }
                } else {
                    Text("More than 1 service domain")
                }
            }
        }
    }
}