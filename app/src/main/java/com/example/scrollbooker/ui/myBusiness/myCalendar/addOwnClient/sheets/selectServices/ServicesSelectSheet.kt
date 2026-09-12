package com.example.scrollbooker.ui.myBusiness.myCalendar.addOwnClient.sheets.selectServices
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.sheet.Sheet
import com.example.scrollbooker.components.core.sheet.SheetHeader
import com.example.scrollbooker.components.customized.productCard.detailSheet.ProductDetailSheet
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.entity.booking.products.domain.model.UserProducts
import com.example.scrollbooker.entity.booking.products.domain.model.toBookingItem
import com.example.scrollbooker.ui.booking.SelectedBookingItem
import com.example.scrollbooker.ui.booking.services.BookingProductsList
import com.example.scrollbooker.ui.profile.sheets.ScheduleShimmer
import com.example.scrollbooker.ui.theme.Divider
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServicesSelectSheet(
    sheetState: SheetState,
    userProducts: FeatureState<UserProducts>,
    linkedItems: List<SelectedBookingItem>,
    onConfirm: (List<SelectedBookingItem>) -> Unit,
    onClose: () -> Unit
) {
    var localLinkedItems by remember(linkedItems) { mutableStateOf(linkedItems) }

    var selectedProduct by remember { mutableStateOf<Product?>(null) }
    val detailSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    fun closeDetail() {
        scope.launch {
            detailSheetState.hide()
            selectedProduct = null
        }
    }

    if (selectedProduct != null) {
        ProductDetailSheet(
            product = selectedProduct,
            selectedBookingItems = localLinkedItems,
            sheetState = detailSheetState,
            onClose = { closeDetail() },
            onAdd = { item ->
                localLinkedItems = localLinkedItems.filterNot { it.productId == item.productId } + item
                closeDetail()
            }
        )
    }

    val isConfirmEnabled = localLinkedItems != linkedItems

    Sheet(
        modifier = Modifier.statusBarsPadding(),
        sheetState = sheetState,
        onClose = onClose,
    ) {
        Column(Modifier.fillMaxSize()) {
            SheetHeader(
                title = stringResource(R.string.linkedServices),
                onClose = onClose
            )

            Box(Modifier.weight(1f)) {
                when (val state = userProducts) {
                    is FeatureState.Loading -> ScheduleShimmer()
                    is FeatureState.Error -> ErrorScreen()
                    is FeatureState.Success -> {
                        BookingProductsList(
                            state = listState,
                            serviceGroups = state.data.data,
                            selectedBookingItems = localLinkedItems,
                            onOpenProductDetail = { product ->
                                selectedProduct = product
                                scope.launch { detailSheetState.show() }
                            },
                            onSelect = { product ->
                                val existing = localLinkedItems.find { it.productId == product.id }

                                when {
                                    existing != null -> localLinkedItems = localLinkedItems - existing

                                    product.variants.size > 1 -> {
                                        selectedProduct = product
                                        scope.launch { detailSheetState.show() }
                                    }

                                    else -> localLinkedItems = localLinkedItems +
                                        product.variants.first().toBookingItem(product)
                                }
                            }
                        )
                    }
                }
            }

            HorizontalDivider(color = Divider, thickness = 0.55.dp)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(BasePadding),
                horizontalArrangement = Arrangement.End
            ) {
                MainButton(
                    title = stringResource(R.string.add),
                    enabled = isConfirmEnabled,
                    onClick = {
                        scope.launch {
                            sheetState.hide()
                            onConfirm(localLinkedItems)
                        }
                    }
                )
            }
        }
    }
}
