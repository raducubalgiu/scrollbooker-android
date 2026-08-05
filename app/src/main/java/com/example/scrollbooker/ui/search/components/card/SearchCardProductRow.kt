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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import com.example.scrollbooker.core.extensions.toTwoDecimals
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXS
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.entity.booking.products.domain.model.getDurationText
import com.example.scrollbooker.entity.booking.products.domain.model.getFiltersSummary
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.SurfaceBG
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

    val productSummaryText = remember(product) {
        val durationText = product.getDurationText(product.startingOffering.duration)
        val filtersSummary = product.getFiltersSummary()

        listOf(durationText, filtersSummary)
            .filter { it.isNotBlank() }
            .joinToString(" • ")
    }

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

        Text(
            text = productSummaryText,
            style = bodyMedium,
            color = Color.Gray
        )
    }
}