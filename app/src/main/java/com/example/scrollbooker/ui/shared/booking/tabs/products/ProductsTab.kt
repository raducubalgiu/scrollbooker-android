package com.example.scrollbooker.ui.shared.booking.tabs.products

import androidx.compose.runtime.Composable
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.ui.shared.products.UserProductsViewModel
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
//    val pagerState = rememberPagerState(initialPage = initialIndex) { 2 }
//    val scope = rememberCoroutineScope()
//
//    Column(
//        modifier = Modifier.fillMaxSize(),
//        verticalArrangement = Arrangement.SpaceBetween
//    ) {
//        Column(Modifier.weight(1f)) {
//            if(initialIndex == 0) {
//                Box(Modifier.padding(BasePadding)) {
//                    SegmentedButtons(
//                        tabs = listOf(
//                            "Ca data trecuta",
//                            stringResource(R.string.allServices)
//                        ),
//                        selectedIndex = pagerState.currentPage,
//                        onClick = {
//                            productsViewModel.clearProducts()
//                            scope.launch { pagerState.animateScrollToPage(it) }
//                        }
//                    )
//                }
//            }
//
//            HorizontalPager(
//                state = pagerState,
//                userScrollEnabled = false,
//                beyondViewportPageCount = 0
//            ) { page ->
//                when(page) {
//                    0 -> Column(Modifier.fillMaxSize()) {
//                        if (initialIndex == 1) return@HorizontalPager
//                        val products = appointmentProducts ?: postProducts
//
//                        when(products) {
//                            is FeatureState.Error -> ErrorScreen()
//                            is FeatureState.Loading -> LoadingScreen()
//                            is FeatureState.Success -> {
//                                val products = products.data
//
//                                LaunchedEffect(products) {
//                                    productsViewModel.setMultipleProducts(products)
//                                }
//
//                                LazyColumn(Modifier.weight(1f)) {
//                                    items(products) {
//                                        ProductCard(
//                                            product = it,
//                                            isSelected = it in selectedProducts,
//                                            onSelect = onSelect
//                                        )
//                                    }
//                                }
//                            }
//                            null -> Unit
//                        }
//                    }
//                    1 -> {
//                        UserProductsServiceTabs(
//                            viewModel = productsViewModel,
//                            selectedProducts = selectedProducts,
//                            userId = userId,
//                            onSelect = onSelect
//                        )
//                    }
//                }
//            }
//        }
//
//        BookingsSheetFooter(
//            selectedProducts = selectedProducts,
//            totalPrice = totalPrice,
//            totalDuration = totalDuration,
//            onNext = onNext
//        )
//    }
}