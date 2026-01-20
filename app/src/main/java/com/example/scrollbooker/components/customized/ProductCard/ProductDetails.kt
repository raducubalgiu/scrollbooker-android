package com.example.scrollbooker.components.customized.ProductCard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.example.scrollbooker.core.enums.FilterTypeEnum
import com.example.scrollbooker.core.extensions.formatDuration
import com.example.scrollbooker.core.extensions.toTwoDecimals
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.entity.booking.products.domain.model.ProductFilter
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.bodyMedium
import com.example.scrollbooker.ui.theme.titleMedium
import java.math.BigDecimal

@Composable
fun ProductDetails(
    modifier: Modifier = Modifier,
    name: String,
    duration: Int,
    price: BigDecimal,
    priceWithDiscount: BigDecimal,
    discount: BigDecimal,
    filters: List<ProductFilter>
) {
    Column(modifier = modifier) {
        Text(
            text = name,
            style = titleMedium,
            fontSize = 18.sp,
            color = OnBackground,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = duration.formatDuration(),
                style = bodyLarge,
                color = Color.Gray
            )
            Text(
                modifier = Modifier.padding(horizontal = 5.dp),
                text = "\u2022",
                color = Color.Gray
            )
            filters.sortedBy { it.id }.mapIndexed { i, filter ->
                when(filter.type) {
                    FilterTypeEnum.OPTIONS -> {
                        filter.subFilters.mapIndexed { subIndex, subFilter ->
                            Text(
                                text = subFilter.name,
                                color = Color.Gray,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            if(subIndex < filter.subFilters.size - 1) {
                                Text(
                                    modifier = Modifier.padding(horizontal = 5.dp),
                                    text = "\u2022",
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                    FilterTypeEnum.RANGE -> {
                        val text = when {
                            filter.minim != null && filter.maxim == null -> "> ${filter.minim}"
                            filter.minim == null && filter.maxim != null -> "< ${filter.maxim}"
                            else -> "${filter.minim} - ${filter.maxim}"
                        }

                        Text(
                            text = "$text ${filter.unit}",
                            color = Color.Gray,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    null -> Unit
                }

                if(i < filters.size - 1) {
                    Text(
                        modifier = Modifier.padding(horizontal = 5.dp),
                        text = "\u2022",
                        color = Color.Gray
                    )
                }
            }
        }

        Spacer(Modifier.height(SpacingS))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${priceWithDiscount.toTwoDecimals()} RON",
                        style = titleMedium,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(Modifier.width(SpacingS))

                    if(discount > BigDecimal.ZERO) {
                        Text(
                            text = price.toTwoDecimals(),
                            style = bodyMedium,
                            textDecoration = TextDecoration.LineThrough
                        )
                        Spacer(Modifier.width(SpacingS))
                        Text(
                            text = "(-${discount.toTwoDecimals()}%)",
                            color = Error
                        )
                    }
                }
            }
        }
    }
}