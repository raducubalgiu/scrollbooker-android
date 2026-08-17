package com.example.scrollbooker.components.customized.productCard.detailSheet
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.scrollbooker.R
import com.example.scrollbooker.components.customized.productCard.ProductVariantCard
import com.example.scrollbooker.core.extensions.formatDuration
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingXS
import com.example.scrollbooker.entity.booking.products.domain.model.ProductVariant
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun ProductDetailVariants(
    variants: List<ProductVariant>,
    selectedVariant: ProductVariant?,
    onSelectVariant: (ProductVariant) -> Unit,
    modifier: Modifier = Modifier
) {
    if (variants.size <= 1) return

    Column(modifier = modifier) {
        Text(
            modifier = Modifier.padding(horizontal = BasePadding),
            text = "${stringResource(R.string.selectAnOption)}*",
            style = titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(SpacingM))

        Column(
            modifier = Modifier.padding(horizontal = BasePadding),
            verticalArrangement = Arrangement.spacedBy(SpacingXS)
        ) {
            variants.forEach { variant ->
                val isSelected = selectedVariant?.id == variant.id

                ProductVariantCard(
                    name = variant.name,
                    durationText = variant.duration.formatDuration(),
                    hasDifferentPrices = variant.hasDifferentPrices,
                    price = variant.startingOffering.price,
                    discount = variant.startingOffering.discount,
                    priceWithDiscount = variant.startingOffering.priceWithDiscount,
                    isSelected = isSelected,
                    isSelectable = true,
                    onSelect = { onSelectVariant(variant) }
                )
            }
        }

        Spacer(Modifier.height(BasePadding))
    }
}