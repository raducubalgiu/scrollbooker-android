package com.example.scrollbooker.ui.search.components.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.core.extensions.formatDuration
import com.example.scrollbooker.core.extensions.toTwoDecimals
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.bodyMedium
import com.example.scrollbooker.ui.theme.titleMedium
import java.math.BigDecimal

@Composable
fun SearchCardProductRow(
    product: Product
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(Modifier.weight(1f)) {
                    Text(
                        text = product.name,
                        style = titleMedium,
                        fontSize = 18.sp,
                        color = OnBackground,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(Modifier.width(BasePadding))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${product.priceWithDiscount.toTwoDecimals()} RON",
                        style = titleMedium,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(Modifier.width(SpacingM))

                    if(product.discount > BigDecimal.ZERO) {
                        Text(
                            text = product.price.toTwoDecimals(),
                            style = bodyMedium,
                            textDecoration = TextDecoration.LineThrough
                        )
                        Spacer(Modifier.width(SpacingS))
                        Text(
                            text = "(-${product.discount.toTwoDecimals()}%)",
                            color = Error
                        )
                    }
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = product.duration.formatDuration(),
                    style = bodyMedium,
                    color = Color.Gray
                )
                Text(
                    modifier = Modifier.padding(horizontal = 5.dp),
                    text = "\u2022",
                    color = Color.Gray
                )
                product.subFilters.mapIndexed { i, subFilter ->
                    Text(
                        text = subFilter.name,
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = bodyMedium
                    )
                    if(i < product.subFilters.size - 1) {
                        Text(
                            modifier = Modifier.padding(horizontal = 5.dp),
                            text = "\u2022",
                            color = Color.Gray
                        )
                    }
                }
            }
        }

//        Row(
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//
//            }
//        }
    }
}