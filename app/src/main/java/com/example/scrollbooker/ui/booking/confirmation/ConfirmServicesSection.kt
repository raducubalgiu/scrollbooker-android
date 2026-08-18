package com.example.scrollbooker.ui.booking.confirmation

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.example.scrollbooker.components.customized.productCard.ProductCardRowPrice
import com.example.scrollbooker.core.extensions.formatDuration
import com.example.scrollbooker.core.extensions.toTwoDecimals
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXXS
import com.example.scrollbooker.ui.booking.BookingTotals
import com.example.scrollbooker.ui.booking.SelectedBookingItem
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun ConfirmServicesSection(
    totals: BookingTotals,
    selectedBookingItems: List<SelectedBookingItem>
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Divider, ShapeDefaults.Medium),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(BasePadding)) {
            selectedBookingItems.forEachIndexed { index, bookingItem ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = bookingItem.productName,
                            style = titleMedium,
                            fontWeight = FontWeight.SemiBold,
                        )

                        Spacer(Modifier.height(SpacingXXS))

                        Text(
                            text = bookingItem.variantDuration.formatDuration(),
                            fontSize = 14.sp,
                            color = Color.Gray
                        )

                        Spacer(Modifier.height(SpacingS))

                        ProductCardRowPrice(
                            hasDifferentOfferings = false,
                            price = bookingItem.offerings.first().price,
                            priceWithDiscount = bookingItem.offerings.first().priceWithDiscount,
                            discount = bookingItem.offerings.first().discount
                        )
                    }
                }

                if(index < selectedBookingItems.size - 1) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = BasePadding),
                        color = Divider,
                        thickness = 0.55.dp
                    )
                }
            }
        }

        HorizontalDivider()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(BasePadding),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = stringResource(R.string.total),
                    style = titleMedium,
                    fontWeight = FontWeight.Bold,
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${totals.totalPrice.toTwoDecimals()} RON",
                    style = titleMedium,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}