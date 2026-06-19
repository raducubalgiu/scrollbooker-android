package com.example.scrollbooker.ui.booking.specialists

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.R
import com.example.scrollbooker.core.extensions.toTwoDecimals
import com.example.scrollbooker.entity.booking.booking.domain.model.BookingFlowUser
import com.example.scrollbooker.entity.booking.products.domain.model.ProductOffering
import com.example.scrollbooker.ui.booking.SelectedBookingItem
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.bodyMedium
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun ProductOfferingCard(
    item: SelectedBookingItem,
    selectedEmployeeId: Int?,
    employees: List<BookingFlowUser>,
    currentOffering: ProductOffering?,
    onRemoveItem: () -> Unit
) {
    val hasOffering = currentOffering != null
    val offerings = item.offerings
    val prices = offerings.map { it.priceWithDiscount }

    val hasPriceVariance = offerings.size > 1 && prices.distinct().size > 1

    val borderColor = if (hasOffering) MaterialTheme.colorScheme.outlineVariant else Color(0xFFEF9A9A)
    val cardBgColor = if (hasOffering) Color.Transparent else Color(0xFFFFEBEE).copy(alpha = 0.2f)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.5.dp,
                color = borderColor,
                shape = RoundedCornerShape(16.dp)
            )
            .background(color = cardBgColor, shape = RoundedCornerShape(16.dp))
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.productName,
                    style = titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "${item.variantDuration} min",
                    style = bodyMedium,
                    color = Color.Gray
                )
            }

            if (hasOffering) {
                Text(
                    text = "${currentOffering.priceWithDiscount.toTwoDecimals()} RON",
                    style = titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val statusColor = if (hasOffering) Error else Color(0xFFF44336)

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .drawBehind {
                            drawCircle(
                                color = statusColor.copy(alpha = 0.1f),
                                radius = size.minDimension / 2 + 4.dp.toPx()
                            )
                        }
                        .background(color = statusColor, shape = CircleShape)
                )
                Text(
                    text = if (hasOffering) stringResource(R.string.availableOnSpecialist)
                           else stringResource(R.string.doesNotOfferThisService),
                    fontWeight = FontWeight.SemiBold,
                    color = statusColor,
                    fontSize = 15.sp
                )
            }

            if (!hasOffering) {
                TextButton(
                    onClick = onRemoveItem,
                    colors = ButtonDefaults.textButtonColors(contentColor = Error)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = stringResource(R.string.delete),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        if (hasPriceVariance) {
            Spacer(modifier = Modifier.height(8.dp))
            PriceVarianceAccordion(
                employees = employees,
                item = item,
                selectedEmployeeId = selectedEmployeeId
            )
        }
    }
}