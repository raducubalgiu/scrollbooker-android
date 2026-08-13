package com.example.scrollbooker.components.customized.productCard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import com.example.scrollbooker.R
import com.example.scrollbooker.core.extensions.formatDuration
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXXS
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.bodyMedium
import com.example.scrollbooker.ui.theme.bodySmall
import com.example.scrollbooker.ui.theme.titleMedium
import java.math.BigDecimal

@Composable
fun ProductVariantCard(
    variantName: String,
    variantDuration: Int,
    price: BigDecimal,
    hasDifferentOfferings: Boolean,
    discount: BigDecimal,
    priceWithDiscount: BigDecimal,
    onEdit: () -> Unit,
    onRemove: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = variantName,
                    style = titleMedium,
                    color = OnBackground,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.height(SpacingXXS))

                Text(
                    text = variantDuration.formatDuration(),
                    style = bodyMedium,
                    color = Color.Gray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.height(SpacingS))

                ProductCardRowPrice(
                    hasDifferentOfferings = hasDifferentOfferings,
                    price = price,
                    discount = discount,
                    priceWithDiscount = priceWithDiscount,
                )
            }

            Spacer(Modifier.width(SpacingS))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(SpacingS)
            ) {
                IconButton(onClick = onEdit) {
                    Icon(
                        painter = painterResource(R.drawable.ic_edit_outline),
                        contentDescription = null,
                        tint = Error
                    )

                }

                IconButton(onClick = onRemove) {
                    Icon(
                        painter = painterResource(R.drawable.ic_delete_outline),
                        contentDescription = null,
                        tint = Error
                    )

                }
            }
        }

        if(hasDifferentOfferings) {
            Spacer(Modifier.height(BasePadding))

            Text(
                text = "Preturile difera in functie de specialist",
                color = Color.Gray,
                style = bodySmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}