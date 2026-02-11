package com.example.scrollbooker.ui.shared.products
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.booking.products.domain.model.Product
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

    Column(modifier = Modifier.fillMaxSize()) {
        when(val sDomainServices = servicesDomainWithServices) {
            is FeatureState.Loading -> Unit
            is FeatureState.Error -> ErrorScreen()
            is FeatureState.Success -> {
                val data = sDomainServices.data

                if(data.size == 1) {
                    ServiceTabs(
                        services = data.first().services,
                        viewModel = viewModel,
                        selectedProducts = selectedProducts,
                        onSelect = onSelect,
                        userId = userId
                    )
                } else {
                    val pagerState = rememberPagerState(initialPage = 0) { data.size }
                    val selectedTabIndex = pagerState.currentPage

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
                        data.forEachIndexed { index, sD ->
                            val isSelected = selectedTabIndex == index

                            FilterChip(
                                selected = isSelected,
                                onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
                                label = {
                                    Text(text = sD.serviceDomain.name)
                                },
                            )
                        }
                    }

                    ServiceTabs(
                        services = data.first().services,
                        viewModel = viewModel,
                        selectedProducts = selectedProducts,
                        onSelect = onSelect,
                        userId = userId
                    )
                }
            }
        }
    }
}