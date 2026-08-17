package com.example.scrollbooker.components.customized.productCard.detailSheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.core.extensions.toTwoDecimals
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.util.Dimens.SpacingXXS
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.bodyMedium
import com.example.scrollbooker.ui.theme.titleLarge
import java.math.BigDecimal

@Composable
fun ProductDetailBottomBar(
    buttonText: String,
    showFromPrefix: Boolean,
    priceWithDiscount: BigDecimal,
    durationText: String,
    isEnabled: Boolean,
    onAddBookingItem: () -> Unit
) {
    val fromText = stringResource(R.string.from)
    val priceText = buildAnnotatedString {
        if (showFromPrefix) {
            withStyle(
                SpanStyle(
                    fontSize = bodyMedium.fontSize,
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray
                )
            ) {
                append("$fromText ")
            }
        }
        withStyle(
            SpanStyle(
                fontSize = titleLarge.fontSize,
                fontWeight = FontWeight.Bold
            )
        ) {
            append("${priceWithDiscount.toTwoDecimals()} RON")
        }
    }

    Column {
        HorizontalDivider(
            color = Divider,
            thickness = 0.55.dp
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(BasePadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = priceText,
                    style = titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(SpacingXXS))

                Text(
                    text = durationText,
                    style = bodyMedium,
                    color = Color.Gray
                )
            }

            Button(
                onClick = onAddBookingItem,
                enabled = isEnabled,
                contentPadding = PaddingValues(
                    vertical = BasePadding,
                    horizontal = SpacingXL
                )
            ) {
                Text(
                    style = bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    text = buttonText,
                )
            }
        }
    }
}