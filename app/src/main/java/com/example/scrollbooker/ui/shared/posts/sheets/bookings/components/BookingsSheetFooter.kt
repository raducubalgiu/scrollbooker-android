package com.example.scrollbooker.ui.shared.posts.sheets.bookings.components

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
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.R
import com.example.scrollbooker.core.extensions.toTwoDecimals
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXS
import com.example.scrollbooker.core.util.Dimens.SpacingXXL
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.titleMedium
import java.math.BigDecimal

@Composable
fun BookingsSheetFooter(
    selectedProducts: Set<Product>,
    totalPrice: BigDecimal,
    totalDuration: Int,
    onNext: () -> Unit
) {
    Column {
        HorizontalDivider(color = Divider, thickness = 0.55.dp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(BasePadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if(selectedProducts.isEmpty()) {
                Text(
                    text = stringResource(R.string.noProductsSelected),
                    style = bodyLarge,
                    color = Color.Gray
                )
            } else {
                Column {
                    if(selectedProducts.isNotEmpty()) {
                        Text(
                            text = "${totalPrice.toTwoDecimals()} RON",
                            fontSize = 20.sp,
                            style = titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(Modifier.height(SpacingXS))

                        Text(
                            text = "${selectedProducts.size} ${stringResource(R.string.services)} \u2022 $totalDuration min",
                            style = bodyLarge,
                            color = Color.Gray
                        )
                    }
                }
            }

            Button(
                shape = ShapeDefaults.Medium,
                contentPadding = PaddingValues(
                    vertical = BasePadding,
                    horizontal = SpacingXXL
                ),
                enabled = totalDuration > 0,
                onClick = onNext
            ) {
                Text(
                    text = stringResource(R.string.next),
                    style = titleMedium,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        }
    }
}