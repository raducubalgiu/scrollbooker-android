package com.example.scrollbooker.ui.appointments.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import com.example.scrollbooker.core.extensions.toTwoDecimals
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXS
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.bodyMedium
import java.math.BigDecimal

@Composable
fun AppointmentProductPrice(
    modifier: Modifier = Modifier,
    name: String,
    subtitle: String? = null,
    price: BigDecimal,
    priceWithDiscount: BigDecimal,
    discount: BigDecimal,
    currencyName: String
) {
    Row(
        modifier = Modifier.fillMaxWidth().then(modifier),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(Modifier.weight(1f)) {
            Text(
                text = name,
                fontWeight = FontWeight.SemiBold
            )

            if (!subtitle.isNullOrBlank()) {
                Text(
                    text = subtitle,
                    style = bodyMedium,
                    color = Color.Gray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            if(discount > BigDecimal.ZERO) {
                Text(
                    text = "(-${discount.toTwoDecimals()}%)",
                    color = Error
                )
                Spacer(Modifier.width(SpacingS))
                Text(
                    text = price.toTwoDecimals(),
                    style = bodyMedium,
                    textDecoration = TextDecoration.LineThrough,
                    color = Color.Gray
                )
            }

            Spacer(Modifier.width(SpacingXS))

            Text(
                text = "${priceWithDiscount.toTwoDecimals()} $currencyName",
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}