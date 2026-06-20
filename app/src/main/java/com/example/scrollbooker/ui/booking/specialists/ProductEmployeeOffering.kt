package com.example.scrollbooker.ui.booking.specialists

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.avatar.Avatar
import com.example.scrollbooker.entity.booking.booking.domain.model.BookingFlowUser
import com.example.scrollbooker.entity.booking.products.domain.model.ProductOffering

@Composable
fun ProductEmployeeOffering(
    isSelected: Boolean,
    employee: BookingFlowUser,
    offering: ProductOffering?,
    rowAlpha: Float,
    rowBgColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(rowAlpha)
            .background(color = rowBgColor, shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Avatar(url = employee.avatar ?: "", size = 28.dp)

            Text(
                text = buildString {
                    append(employee.fullName)
                    if (isSelected) append(" (${stringResource(R.string.selected)})")
                },
                fontSize = 14.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            )
        }

        Text(
            text = offering?.let { "${it.priceWithDiscount} RON" } ?: "N/A",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}