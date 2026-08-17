package com.example.scrollbooker.components.customized.productCard.detailSheet
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.sheet.Sheet
import com.example.scrollbooker.components.core.sheet.SheetHeader
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.entity.booking.products.domain.model.ProductVariant
import com.example.scrollbooker.entity.booking.products.domain.model.getDurationText
import com.example.scrollbooker.entity.booking.products.domain.model.toBookingItem
import com.example.scrollbooker.ui.booking.SelectedBookingItem
import java.math.BigDecimal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailSheet(
    product: Product?,
    selectedBookingItems: List<SelectedBookingItem>,
    sheetState: SheetState,
    onAdd: (SelectedBookingItem) -> Unit,
    onClose: () -> Unit
) {
    var selectedVariant by remember { mutableStateOf<ProductVariant?>(null) }

    val alreadySelectedItem = remember(product, selectedBookingItems) {
        selectedBookingItems.find { it.productId == product?.id }
    }

    LaunchedEffect(product, alreadySelectedItem) {
        if (product != null) {
            when {
                alreadySelectedItem != null -> {
                    selectedVariant = product.variants.find { it.id == alreadySelectedItem.variantId }
                }
                product.variants.size == 1 -> {
                    selectedVariant = product.variants.firstOrNull()
                }
                else -> {
                    selectedVariant = null
                }
            }
        }
    }

    val isButtonEnabled = remember(product, selectedVariant, alreadySelectedItem) {
        derivedStateOf {
            if (product == null) return@derivedStateOf false

            val hasSelectedOption = selectedVariant != null
            val isCurrentVariantAlreadyInCart = alreadySelectedItem != null &&
                    alreadySelectedItem.variantId == selectedVariant?.id

            when {
                isCurrentVariantAlreadyInCart -> false
                product.variants.size == 1 -> true
                else -> hasSelectedOption
            }
        }
    }

    val buttonText = when {
        alreadySelectedItem != null && selectedVariant?.id != alreadySelectedItem.variantId -> stringResource(R.string.update)
        alreadySelectedItem != null -> stringResource(R.string.added)
        else -> stringResource(R.string.add)
    }

    val displayedOffering = remember(product, selectedVariant) {
        selectedVariant?.startingOffering ?: product?.startingOffering
    }

    val hasMultiplePrices = remember(product, selectedVariant) {
        selectedVariant?.hasDifferentPrices ?: product?.hasDifferentPrices ?: false
    }

    Sheet(
        modifier = Modifier.statusBarsPadding(),
        sheetState = sheetState,
        onClose = onClose,
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            SheetHeader(
                title = "",
                onClose = onClose
            )

            if(product != null) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(BasePadding)) {
                        ProductDetailInfo(
                            name = product.name,
                            description = product.description
                        )

                        ProductDetailFilters(product.filters)

                        ProductDetailVariants(
                            variants = product.variants,
                            selectedVariant = selectedVariant,
                            onSelectVariant = { selectedVariant = it }
                        )
                    }
                }

                ProductDetailBottomBar(
                    buttonText = buttonText,
                    priceWithDiscount = displayedOffering?.priceWithDiscount ?: BigDecimal.ZERO,
                    showFromPrefix = hasMultiplePrices,
                    durationText = product.getDurationText(product.startingOffering.duration),
                    isEnabled = isButtonEnabled.value,
                    onAddBookingItem = {
                        val targetVariant = selectedVariant ?: product.variants.first()
                        val bookingItem = targetVariant.toBookingItem(product)

                        onAdd(bookingItem)
                    }
                )
            }
        }
    }
}