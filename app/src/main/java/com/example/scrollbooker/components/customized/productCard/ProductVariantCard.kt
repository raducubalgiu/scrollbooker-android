package com.example.scrollbooker.components.customized.productCard

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingXXS
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.bodyMedium
import java.math.BigDecimal

@Composable
fun ProductVariantCard(
    modifier: Modifier = Modifier,
    name: String,
    durationText: String,
    hasDifferentPrices: Boolean,
    price: BigDecimal,
    discount: BigDecimal,
    priceWithDiscount: BigDecimal,
    isSelected: Boolean,
    isSelectable: Boolean = false,
    isEditable: Boolean = false,
    isDeletable: Boolean = false,
    onSelect: () -> Unit = {},
    onEdit: () -> Unit = {},
    onDelete: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(ShapeDefaults.Medium)
            .border(
                width = if (isSelected) 1.5.dp else 1.dp,
                color = if (isSelected) Primary else Divider,
                shape = ShapeDefaults.Medium
            )
            .let { if (isSelectable) it.clickable { onSelect() } else it }
            .padding(horizontal = BasePadding, vertical = SpacingM),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text(
                text = name,
                style = bodyLarge,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(SpacingXXS))

            Text(
                text = durationText,
                style = bodyMedium,
                color = Color.Gray
            )

            Spacer(Modifier.height(BasePadding))

            ProductCardRowPrice(
                hasDifferentOfferings = hasDifferentPrices,
                price = price,
                discount = discount,
                priceWithDiscount = priceWithDiscount
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            if (isEditable) {
                IconButton(onClick = onEdit) {
                    Icon(
                        painter = painterResource(R.drawable.ic_edit_outline),
                        contentDescription = stringResource(R.string.edit)
                    )
                }
            }

            if (isDeletable) {
                IconButton(onClick = onDelete) {
                    Icon(
                        painter = painterResource(R.drawable.ic_delete_outline),
                        contentDescription = stringResource(R.string.delete),
                        tint = Error
                    )
                }
            }

            if (isSelectable) {
                RadioButton(
                    selected = isSelected,
                    onClick = onSelect,
                    colors = RadioButtonColors(
                        selectedColor = OnBackground,
                        unselectedColor = Divider,
                        disabledSelectedColor = Divider,
                        disabledUnselectedColor = Divider
                    )
                )
            }
        }
    }
}