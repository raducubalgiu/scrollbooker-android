package com.example.scrollbooker.ui.booking
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.scrollbooker.components.core.headers.Header
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.components.customized.ProductCard.ProductDetailSheet
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.entity.booking.products.domain.model.UserProducts
import com.example.scrollbooker.ui.booking.services.BookingProductsList
import com.example.scrollbooker.ui.booking.services.BookingServicesTabs
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingServicesScreen(
    modifier: Modifier = Modifier,
    viewModel: BookingViewModel,
    onNavigateToSpecialists: () -> Unit,
    onBack: () -> Unit
) {
    val productsState by viewModel.productsState.collectAsStateWithLifecycle()
    val selectedBookingItems by viewModel.selectedBookingItems.collectAsStateWithLifecycle()
    val bookingTotals by viewModel.bookingTotals.collectAsStateWithLifecycle()
    val isInitialProductProcessed by viewModel.isInitialProductProcessed.collectAsStateWithLifecycle()

    var selectedProduct by remember { mutableStateOf<Product?>(null) }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    if (productsState is FeatureState.Success && !isInitialProductProcessed && viewModel.initialSelectedProductId != -1) {
        val serviceGroups = (productsState as FeatureState.Success<UserProducts>).data.data

        LaunchedEffect(serviceGroups) {
            val targetProduct = serviceGroups
                .flatMap { it.products }
                .find { it.id == viewModel.initialSelectedProductId }

            if (targetProduct != null) {
                if (targetProduct.variants.size > 1) {
                    selectedProduct = targetProduct
                    sheetState.show()
                } else {
                    val variant = targetProduct.variants.first()
                    val bookingItem = SelectedBookingItem(
                        productId = targetProduct.id,
                        variantId = variant.id,
                        variantDuration = variant.duration,
                        offerings = variant.offerings,
                        productName = targetProduct.name,
                        variantName = variant.name
                    )
                    viewModel.selectBookingItem(bookingItem)
                }
            }
            viewModel.markInitialProductAsProcessed()
        }
    }

    if(sheetState.isVisible) {
        ProductDetailSheet(
            product = selectedProduct,
            sheetState = sheetState,
            onClose = { scope.launch { sheetState.hide() } },
            onAdd = {
                viewModel.selectBookingItem(it)
                scope.launch { sheetState.hide() }
            }
        )
    }

    val listState = rememberLazyListState()

    Scaffold(
        modifier = modifier,
        topBar = { Header(onBack = onBack) },
    ) { innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
        ) {
            when (val state = productsState) {
                is FeatureState.Error -> ErrorScreen()
                is FeatureState.Loading -> LoadingScreen()
                is FeatureState.Success -> {
                    val serviceGroups = state.data.data

                    val activeTabIndexProvider = remember(serviceGroups) {
                        derivedStateOf {
                            val visibleItems = listState.layoutInfo.visibleItemsInfo
                            if (visibleItems.isEmpty()) {
                                0
                            } else {
                                val firstVisibleItem = visibleItems.firstOrNull { item ->
                                    item.offset <= 0 && item.offset + item.size > 0
                                } ?: visibleItems.first()

                                firstVisibleItem.index.coerceIn(0, serviceGroups.lastIndex)
                            }
                        }
                    }

                    Column(modifier = Modifier.fillMaxSize()) {
                        Column(Modifier.weight(1f)) {
                            if (serviceGroups.isNotEmpty()) {
                                BookingServicesTabs(
                                    activeTabIndex = activeTabIndexProvider.value,
                                    onTabChange = { tabIndex ->
                                        scope.launch {
                                            listState.animateScrollToItem(tabIndex)
                                        }
                                    },
                                    serviceGroups = serviceGroups
                                )
                            }

                            BookingProductsList(
                                state = listState,
                                serviceGroups = serviceGroups,
                                selectedBookingItems = selectedBookingItems,
                                onSelect = { product ->
                                    if(product.variants.size > 1) {
                                        selectedProduct = product
                                        scope.launch { sheetState.show() }
                                    } else {
                                        val variant = product.variants.first()
                                        val bookingItem = SelectedBookingItem(
                                            productId = product.id,
                                            variantId = variant.id,
                                            variantDuration = variant.duration,
                                            offerings = variant.offerings,
                                            productName = product.name,
                                            variantName = variant.name
                                        )

                                        viewModel.selectBookingItem(bookingItem)
                                    }
                                },
                                onOpenProductDetail = {
                                    selectedProduct = it
                                    scope.launch { sheetState.show() }
                                },
                            )
                        }

                        BookingBottomBar(
                            bookingTotals = bookingTotals,
                            isVisible = selectedBookingItems.isNotEmpty(),
                            onNavigateToSpecialists = onNavigateToSpecialists
                        )
                    }
                }
            }
        }
    }
}