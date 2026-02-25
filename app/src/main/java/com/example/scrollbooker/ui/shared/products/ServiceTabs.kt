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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.EmptyScreen
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.model.ServiceWithEmployees
import com.example.scrollbooker.ui.shared.products.components.ServiceTab
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import kotlinx.coroutines.launch

@Composable
fun ServiceTabs(
    viewModel: UserProductsViewModel,
    selectedProducts: Set<Product>,
    onSelect: (Product) -> Unit,
    userId: Int,
    services: List<ServiceWithEmployees>
) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(initialPage = 0) { services.size }
    val selectedTabIndex = pagerState.currentPage

    LaunchedEffect(userId) {
        viewModel.setUserId(userId)
    }

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
            services.forEachIndexed { index, service ->
                val isSelected = selectedTabIndex == index

                ServiceTab(
                    isSelected = isSelected,
                    serviceName = service.shortName,
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
            val serviceId = services[page].id
            val employees = services[page].employees

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
}