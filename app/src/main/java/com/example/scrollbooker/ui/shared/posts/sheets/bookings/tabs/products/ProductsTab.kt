package com.example.scrollbooker.ui.shared.posts.sheets.bookings.tabs.products

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.SegmentedButtons
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.components.customized.ProductCard.ProductCard
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.ui.shared.posts.sheets.bookings.components.BookingsSheetFooter
import com.example.scrollbooker.ui.shared.products.UserProductsServiceTabs
import com.example.scrollbooker.ui.shared.products.UserProductsViewModel
import kotlinx.coroutines.launch
import java.math.BigDecimal

@Composable
fun ProductsTab(
    productsViewModel: UserProductsViewModel,
    appointmentProducts: FeatureState<List<Product>>?,
    postProducts:  FeatureState<List<Product>>?,
    initialIndex: Int = 1,
    selectedProducts: Set<Product>,
    onSelect: (Product) -> Unit,
    totalPrice: BigDecimal,
    totalDuration: Int,
    userId: Int,
    onNext: () -> Unit
) {
    val pagerState = rememberPagerState(initialPage = initialIndex) { 2 }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(Modifier.weight(1f)) {
            if(initialIndex == 0) {
                Box(Modifier.padding(BasePadding)) {
                    SegmentedButtons(
                        tabs = listOf(
                            "Ca data trecuta",
                            stringResource(R.string.allServices)
                        ),
                        selectedIndex = pagerState.currentPage,
                        onClick = {
                            productsViewModel.clearProducts()
                            scope.launch { pagerState.animateScrollToPage(it) }
                        }
                    )
                }
            }

            HorizontalPager(
                state = pagerState,
                userScrollEnabled = false,
                beyondViewportPageCount = 0
            ) { page ->
                when(page) {
                    0 -> Column(Modifier.fillMaxSize()) {
                        if (initialIndex == 1) return@HorizontalPager
                        val products = appointmentProducts ?: postProducts

                        when(products) {
                            is FeatureState.Error -> ErrorScreen()
                            is FeatureState.Loading -> LoadingScreen()
                            is FeatureState.Success -> {
                                val products = products.data

                                LaunchedEffect(products) {
                                    productsViewModel.setMultipleProducts(products)
                                }

                                LazyColumn(Modifier.weight(1f)) {
                                    items(products) {
                                        ProductCard(
                                            product = it,
                                            isSelected = it in selectedProducts,
                                            onSelect = onSelect
                                        )
                                    }
                                }
                            }
                            null -> Unit
                        }
                    }
                    1 -> {
                        UserProductsServiceTabs(
                            viewModel = productsViewModel,
                            selectedProducts = selectedProducts,
                            userId = userId,
                            onSelect = onSelect
                        )
                    }
                }
            }
        }

        BookingsSheetFooter(
            selectedProducts = selectedProducts,
            totalPrice = totalPrice,
            totalDuration = totalDuration,
            onNext = onNext
        )
    }
}