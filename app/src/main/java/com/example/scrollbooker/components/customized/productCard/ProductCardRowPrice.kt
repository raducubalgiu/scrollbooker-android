package com.example.scrollbooker.components.customized.productCard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.core.extensions.toTwoDecimals
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.bodyMedium
import com.example.scrollbooker.ui.theme.titleMedium
import java.math.BigDecimal

@Composable
fun ProductCardRowPrice(
    price: BigDecimal,
    priceWithDiscount: BigDecimal,
    discount: BigDecimal
) {
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
                    fontSize = 16.sp,
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
                        color = Error,
                        style = titleMedium,
                        fontSize = 16.sp,
                    )
                }
            }
        }
    }
}