package com.example.scrollbooker.ui.booking.services
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.EmptyScreen
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.components.customized.productCard.detailSheet.ProductDetailSheet
import com.example.scrollbooker.core.snackbar.CustomSnackBar
import com.example.scrollbooker.core.snackbar.rememberSnackBarController
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.booking.booking.domain.model.BookingFlow
import com.example.scrollbooker.entity.booking.products.domain.model.BusinessServicesWithProducts
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.entity.booking.products.domain.model.toBookingItem
import com.example.scrollbooker.navigation.navigators.BookingNavigator
import com.example.scrollbooker.ui.booking.BookingLayout
import com.example.scrollbooker.ui.booking.BookingViewModel
import kotlinx.coroutines.launch

private fun findProductFlattenedIndex(
    productId: Int,
    serviceGroups: List<BusinessServicesWithProducts>
): Int {
    var index = 0
    for (group in serviceGroups) {
        index++ // section header item
        val productIndex = group.products.indexOfFirst { it.id == productId }
        if (productIndex != -1) {
            return index + productIndex
        }
        index += group.products.size
    }
    return 0
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingServicesScreen(
    modifier: Modifier = Modifier,
    viewModel: BookingViewModel,
    bookingNavigate: BookingNavigator
) {
    val bookingFlowState by viewModel.bookingFlowState.collectAsStateWithLifecycle()
    val selectedBookingItems by viewModel.selectedBookingItems.collectAsStateWithLifecycle()
    val bookingTotals by viewModel.bookingTotals.collectAsStateWithLifecycle()
    val isInitialSelectionProcessed by viewModel.isInitialSelectionProcessed.collectAsStateWithLifecycle()

    var selectedProduct by remember { mutableStateOf<Product?>(null) }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    val snackBarHostState = remember { SnackbarHostState() }
    val snackBarController = rememberSnackBarController(snackBarHostState)

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            snackBarController.show(event)
        }
    }

    if (
        bookingFlowState is FeatureState.Success &&
        !isInitialSelectionProcessed &&
        (viewModel.appointmentId != -1 || viewModel.initialSelectedProductId != -1)
    ) {
        val bookingFlow = (bookingFlowState as FeatureState.Success<BookingFlow>).data
        val serviceGroups = bookingFlow.products.data

        LaunchedEffect(serviceGroups) {
            val targetProductId = if (viewModel.appointmentId != -1) {
                viewModel.processAppointmentRebooking(bookingFlow)
            } else {
                val targetProduct = serviceGroups
                    .flatMap { it.products }
                    .find { it.id == viewModel.initialSelectedProductId }

                if (targetProduct != null) {
                    if (targetProduct.variants.size > 1) {
                        selectedProduct = targetProduct
                        sheetState.show()
                    } else {
                        val bookingItem = targetProduct.variants.first().toBookingItem(targetProduct)
                        viewModel.selectBookingItem(bookingItem)
                    }
                }

                targetProduct?.id
            }

            if (targetProductId != null) {
                listState.scrollToItem(findProductFlattenedIndex(targetProductId, serviceGroups))
            }
            viewModel.markInitialSelectionAsProcessed()
        }
    }

    if (selectedProduct != null) {
        ProductDetailSheet(
            product = selectedProduct,
            selectedBookingItems = selectedBookingItems,
            sheetState = sheetState,
            onClose = {
                scope.launch {
                    sheetState.hide()
                    selectedProduct = null
                }
            },
            onAdd = {
                viewModel.selectBookingItem(it)
                scope.launch {
                    sheetState.hide()
                    selectedProduct = null
                }
            }
        )
    }

    Box(Modifier.fillMaxSize()) {
        BookingLayout(
            modifier = modifier,
            title = stringResource(R.string.chooseServices),
            onBack = { bookingNavigate.back() },
            onNext = {
                val bookingFlow = (bookingFlowState as FeatureState.Success<BookingFlow>).data

                if (bookingFlow.business.hasEmployees && !viewModel.isEmployee) {
                    bookingNavigate.toSpecialists()
                } else {
                    bookingNavigate.toDateTime()
                }
            },
            bookingTotals = bookingTotals,
            displayBottomBar = selectedBookingItems.isNotEmpty()
        ) {
            when (val state = bookingFlowState) {
                is FeatureState.Error -> ErrorScreen()
                is FeatureState.Loading -> LoadingScreen()
                is FeatureState.Success -> {
                    val serviceGroups = state.data.products.data
                    val totalCount = state.data.products.totalCount

                    if (totalCount == 0) {
                        EmptyScreen(
                            modifier = Modifier.padding(top = 50.dp),
                            arrangement = Arrangement.Top,
                            message = stringResource(R.string.notFoundServices),
                            icon = painterResource(R.drawable.ic_shopping_outline)
                        )
                    } else {
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

                        Column(Modifier.fillMaxSize()) {
                            if (serviceGroups.isNotEmpty()) {
                                BookingServicesTabs(
                                    activeTabIndexProvider = { activeTabIndexProvider.value },
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
                                    val existingSelectedItem = selectedBookingItems.find { it.productId == product.id }

                                    if (existingSelectedItem != null) {
                                        viewModel.selectBookingItem(existingSelectedItem)
                                    } else {
                                        if (product.variants.size > 1) {
                                            selectedProduct = product
                                            scope.launch { sheetState.show() }
                                        } else {
                                            val bookingItem =
                                                product.variants.first().toBookingItem(product)
                                            viewModel.selectBookingItem(bookingItem)
                                        }
                                    }
                                },
                                onOpenProductDetail = {
                                    selectedProduct = it
                                    scope.launch { sheetState.show() }
                                },
                            )
                        }
                    }
                }
            }
        }

        CustomSnackBar(hostState = snackBarHostState)
    }
}