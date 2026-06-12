package com.example.scrollbooker.components.customized.ProductCard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.inputs.InputRadio
import com.example.scrollbooker.components.core.sheet.SheetHeader
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.util.Dimens.SpacingXXS
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.entity.booking.products.domain.model.ProductVariant
import com.example.scrollbooker.entity.booking.products.domain.model.getDurationText
import com.example.scrollbooker.entity.booking.products.domain.model.toBookingItem
import com.example.scrollbooker.ui.booking.SelectedBookingItem
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.bodyMedium
import com.example.scrollbooker.ui.theme.headlineMedium
import com.example.scrollbooker.ui.theme.titleLarge
import com.example.scrollbooker.ui.theme.titleMedium

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

    ModalBottomSheet(
        modifier = Modifier.statusBarsPadding(),
        sheetState = sheetState,
        onDismissRequest = onClose,
        containerColor = Background,
        contentColor = OnBackground,
        dragHandle = {},
        contentWindowInsets = { BottomSheetDefaults.windowInsets },
        properties = ModalBottomSheetProperties(
            shouldDismissOnBackPress = false
        ),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            SheetHeader(
                title = "",
                onClose = onClose
            )

            if(product != null) {
                Column(Modifier.weight(1f)) {
                    Column(Modifier.padding(horizontal = SpacingXL)) {
                        Text(
                            modifier = Modifier.padding(bottom = SpacingXXS),
                            text = product.name,
                            style = headlineMedium,
                            fontWeight = FontWeight.SemiBold
                        )

                        if(product.description.isNullOrBlank()) {
                            Text(
                                text = stringResource(R.string.serviceWithoutDescription),
                                style = bodyMedium,
                                color = Color.Gray
                            )
                        } else {
                            Text(
                                text = product.description,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    Spacer(Modifier.height(BasePadding))

                    product.filters.forEach { filter ->
                        Column(
                            modifier = Modifier
                                .padding(bottom = 12.dp)
                                .padding(horizontal = SpacingXL)
                        ) {

                            Text(
                                text = filter.name,
                                style = titleMedium,
                                modifier = Modifier.padding(bottom = 6.dp),
                                fontWeight = FontWeight.SemiBold
                            )

                            FlowRow {
                                filter.subFilters.forEach { sub ->
                                    TooltipBox(
                                        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                                        tooltip = {
                                            sub.description?.let { desc ->
                                                Text(
                                                    text = desc,
                                                    modifier = Modifier.padding(8.dp)
                                                )
                                            }
                                        },
                                        state = rememberTooltipState()
                                    ) {
                                        SuggestionChip(
                                            onClick = {},
                                            label = {
                                                Text(
                                                    text = sub.name,
                                                    fontWeight = FontWeight.SemiBold,
                                                    fontSize = 16.sp
                                                )
                                            },
                                            shape = ShapeDefaults.ExtraLarge,
                                            colors = SuggestionChipDefaults.suggestionChipColors(
                                                containerColor = Color.Transparent
                                            ),
                                            border = SuggestionChipDefaults.suggestionChipBorder(
                                                enabled = true,
                                                borderColor = Divider,
                                                borderWidth = 1.dp
                                            ),
                                            modifier = Modifier.padding(end = 8.dp, bottom = 8.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(BasePadding))

                    if(product.variants.size > 1) {
                        Text(
                            modifier = Modifier.padding(horizontal = SpacingXL),
                            text = "${stringResource(R.string.selectAnOption)}*",
                            style = titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        product.variants.mapIndexed { index, variant ->
                            InputRadio(
                                selected = selectedVariant?.id == variant.id,
                                onSelect = { selectedVariant = variant },
                                headLine = variant.name
                            )

                            if(index < product.variants.size - 1) {
                                HorizontalDivider(
                                    color = Divider,
                                    thickness = 0.55.dp
                                )
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = SpacingXL,
                            end = SpacingXL,
                            bottom = BasePadding
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "${product.startingOffering.priceWithDiscount} RON",
                            style = titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(Modifier.height(SpacingXXS))

                        Text(
                            text = product.getDurationText(product.startingOffering.duration),
                            style = bodyMedium,
                            color = Color.Gray
                        )
                    }

                    Button(
                        onClick = {
                            val targetVariant = selectedVariant ?: product.variants.first()
                            val bookingItem = targetVariant.toBookingItem(product)

                            onAdd(bookingItem)
                        },
                        enabled = isButtonEnabled.value,
                        contentPadding = PaddingValues(
                            vertical = BasePadding,
                            horizontal = SpacingXL
                        )
                    ) {
                        val buttonText = when {
                            alreadySelectedItem != null && selectedVariant?.id != alreadySelectedItem.variantId -> stringResource(R.string.update)
                            alreadySelectedItem != null -> stringResource(R.string.added)
                            else -> stringResource(R.string.add)
                        }

                        Text(
                            style = bodyLarge,
                            fontWeight = FontWeight.SemiBold,
                            text = buttonText,
                        )
                    }
                }
            }
        }
    }
}