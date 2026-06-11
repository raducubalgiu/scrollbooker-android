package com.example.scrollbooker.ui.profile.tabs.products
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.components.core.buttons.MainButtonOutlined
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.components.customized.ProductCard.ProductCard
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.booking.products.domain.model.UserProducts
import com.example.scrollbooker.ui.booking.services.BookingServicesTabs
import com.example.scrollbooker.ui.theme.Divider
import kotlinx.coroutines.launch

@Composable
fun ProfileProductsTab(
    products: FeatureState<UserProducts>
) {
    val scope = rememberCoroutineScope()

    when (val state = products) {
        is FeatureState.Error -> ErrorScreen()
        is FeatureState.Loading -> LoadingScreen()
        is FeatureState.Success -> {
            val serviceGroups = state.data.data
            val totalCount = state.data.totalCount

            Column(modifier = Modifier.fillMaxSize()) {
                if (serviceGroups.isNotEmpty()) {
                    val pagerState = rememberPagerState { serviceGroups.size }

                    BookingServicesTabs(
                        activeTabIndex = pagerState.currentPage,
                        onTabChange = { tabIndex ->
                            scope.launch {
                                pagerState.animateScrollToPage(tabIndex)
                            }
                        },
                        serviceGroups = serviceGroups
                    )

                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.weight(1f)
                    ) { page ->
                        val currentGroupProducts = serviceGroups[page].products

                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(vertical = BasePadding)
                        ) {
                            itemsIndexed(currentGroupProducts) { index, product ->
                                ProductCard(
                                    modifier = Modifier.padding(horizontal = BasePadding),
                                    product = product
                                )

                                if (index < currentGroupProducts.lastIndex) {
                                    HorizontalDivider(
                                        modifier = Modifier.padding(vertical = BasePadding),
                                        color = Divider,
                                        thickness = 0.55.dp
                                    )
                                }
                            }

                            item {
                                val shouldShowViewMore = (serviceGroups.size * 5) < totalCount

                                if (shouldShowViewMore) {
                                    MainButtonOutlined(
                                        modifier = Modifier.padding(BasePadding),
                                        title = "Vezi toate cele $totalCount servicii",
                                        onClick = {}
                                    )
                                }
                            }
                        }
                    }
                } else {
                    // Opțional: Un ecran/text pentru cazul în care lista este goală

                }
            }
        }
    }
}