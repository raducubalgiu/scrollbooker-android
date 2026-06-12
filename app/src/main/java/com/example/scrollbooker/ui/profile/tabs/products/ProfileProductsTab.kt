package com.example.scrollbooker.ui.profile.tabs.products
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.components.core.buttons.MainButtonOutlined
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.components.customized.ProductCard.ProductCard
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.entity.booking.products.domain.model.UserProducts
import com.example.scrollbooker.ui.booking.services.BookingServicesTabs
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.SurfaceBG
import kotlinx.coroutines.launch

@Composable
fun ProfileProductsTab(
    products: FeatureState<UserProducts>,
    onNavigateToBookingFromProduct: (product: Product) -> Unit,
    onNavigateToBookingFromProfile: () -> Unit
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

                    ScrollableTabRow(
                        selectedTabIndex = pagerState.currentPage,
                        containerColor = Background,
                        divider = {},
                        indicator = { _ -> Box(Modifier.size(0.dp)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        serviceGroups.forEachIndexed { index, group ->
                            val isSelected = pagerState.currentPage == index

                            Tab(
                                selected = isSelected,
                                onClick = {
                                    scope.launch {
                                        pagerState.animateScrollToPage(index)
                                    }
                                },
                                modifier = Modifier
                                    .clip(ShapeDefaults.Medium)
                                    .background(if (isSelected) SurfaceBG else Background)
                                    .height(42.dp),
                                text = {
                                    val tabTitle = group.service.shortName
                                    Text(
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                        text = tabTitle,
                                        fontSize = 16.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) OnSurfaceBG else OnBackground,
                                    )
                                }
                            )
                        }
                    }

                    HorizontalDivider(
                        color = Divider,
                        thickness = 0.55.dp
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
                                    product = product,
                                    onOpenProductDetail = {},
                                    onNavigateToBooking = onNavigateToBookingFromProduct
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