package com.example.scrollbooker.ui.search.components.card
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
import com.example.scrollbooker.core.enums.ProductTypeEnum
import com.example.scrollbooker.core.extensions.formatDuration
import com.example.scrollbooker.core.extensions.toTwoDecimals
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXS
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.bodyMedium
import com.example.scrollbooker.ui.theme.bodySmall
import com.example.scrollbooker.ui.theme.titleMedium
import java.math.BigDecimal

data class TimeSlot(
    val id: Int,
    val name: String,
    val isLastMinute: Boolean
)

@Composable
fun SearchCardProductRow(
    product: Product
) {
    val timeSlots = listOf(
        TimeSlot(id = 1, name = "09:00", isLastMinute = false),
        TimeSlot(id = 1, name = "12:00", isLastMinute = true),
        TimeSlot(id = 1, name = "13:00", isLastMinute = false),
        TimeSlot(id = 1, name = "15:00", isLastMinute = false),
        TimeSlot(id = 1, name = "17:00", isLastMinute = false)
    )

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

            Spacer(Modifier.height(SpacingXS))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = product.duration.formatDuration(),
                    style = bodyLarge,
                    color = Color.Gray
                )

                if(product.type == ProductTypeEnum.PACK && product.sessionsCount != null) {
                    Text(
                        modifier = Modifier.padding(horizontal = 5.dp),
                        text = "\u2022 ${product.sessionsCount} ședinte",
                        color = Color.Gray
                    )
                }

                if(product.filters.isNotEmpty()) {
                    Text(
                        modifier = Modifier.padding(horizontal = 5.dp),
                        text = "\u2022",
                        color = Color.Gray
                    )
                }

                product.filters.mapIndexed { i, filter ->
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
                                        text = "&",
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

                    if(i < product.filters.size - 1) {
                        Text(
                            modifier = Modifier.padding(horizontal = 5.dp),
                            text = "\u2022",
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }

    if(product.description != null && product.description.isNotEmpty()) {
        Spacer(Modifier.height(SpacingS))

        Text(
            text = product.description,
            color = Color.Gray,
            style = bodySmall,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }

//    LazyRow(
//        contentPadding = PaddingValues(
//            top = BasePadding
//        )
//    ) {
//        itemsIndexed(timeSlots) { index, slot ->
//            Column(
//                modifier = Modifier
//                    .clip(shape = ShapeDefaults.Medium)
//                    .background(if(slot.isLastMinute) Color(0xFFb7faf8) else SurfaceBG)
//                    .padding(vertical = SpacingS, horizontal = BasePadding)
//            ) {
//                Row(
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Text(
//                        text = slot.name,
//                        color = OnSurfaceBG,
//                        fontWeight = FontWeight.SemiBold
//                    )
//
//                    if(slot.isLastMinute) {
//                        Spacer(Modifier.width(SpacingS))
//
//                        Text(
//                            text = "(-10%)",
//                            style = bodySmall,
//                            fontWeight = FontWeight.SemiBold,
//                            color = Error
//                        )
//                    }
//                }
//            }
//
//            if(index < timeSlots.size - 1) {
//                Spacer(Modifier.width(SpacingS))
//            }
//        }
//    }
}