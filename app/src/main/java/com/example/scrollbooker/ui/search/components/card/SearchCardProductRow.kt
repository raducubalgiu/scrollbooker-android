package com.example.scrollbooker.ui.search.components.card
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
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
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.bodyMedium
import com.example.scrollbooker.ui.theme.titleMedium
import java.math.BigDecimal

@Composable
fun SearchCardProductRow(
    modifier: Modifier = Modifier,
    product: Product,
    onSelectProduct: (product: Product) -> Unit
) {
    val startingOffering = product.startingOffering

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape = ShapeDefaults.Medium)
            .clickable { onSelectProduct(product) }
            .background(SurfaceBG)
            .padding(SpacingM)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = product.name,
                style = titleMedium,
                fontSize = 16.sp,
                color = OnBackground,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.width(BasePadding))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(SpacingS)
            ) {
                Text(
                    text = "${startingOffering.priceWithDiscount.toTwoDecimals()} RON",
                    style = titleMedium,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = OnBackground
                )

                if (startingOffering.discount > BigDecimal.ZERO) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = startingOffering.price.toTwoDecimals(),
                            style = bodyMedium,
                            textDecoration = TextDecoration.LineThrough,
                            color = Color.Gray
                        )
                        Spacer(Modifier.width(SpacingS))
                        Text(
                            text = "(-${startingOffering.discount.toTwoDecimals()}%)",
                            style = bodyMedium,
                            color = Error,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(SpacingXS))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = startingOffering.duration.formatDuration(),
                style = bodyLarge,
                color = Color.Gray
            )

            if (product.type == ProductTypeEnum.PACK && product.sessionsCount != null) {
                Text(
                    text = "  \u2022  ${product.sessionsCount} ședințe",
                    style = bodyLarge,
                    color = Color.Gray
                )
            }

            if (product.filters.isNotEmpty()) {
                Text(
                    text = "  \u2022  ",
                    style = bodyLarge,
                    color = Color.Gray
                )
            }

            product.filters.forEachIndexed { i, filter ->
                when (filter.type) {
                    FilterTypeEnum.OPTIONS -> {
                        filter.subFilters.forEachIndexed { subIndex, subFilter ->
                            Text(
                                text = subFilter.name,
                                style = bodyLarge,
                                color = Color.Gray,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            if (subIndex < filter.subFilters.size - 1) {
                                Text(text = " & ", style = bodyLarge, color = Color.Gray)
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
                            style = bodyLarge,
                            color = Color.Gray,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    null -> Unit
                }

                if (i < product.filters.size - 1) {
                    Text(text = "  \u2022  ", style = bodyLarge, color = Color.Gray)
                }
            }
        }
    }
}