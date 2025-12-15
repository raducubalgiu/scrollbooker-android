package com.example.scrollbooker.ui.myBusiness.myCalendar.components.slot

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun SlotIsBooked(
    title: String,
    maxLines: Int
) {
    Text(
        text = title,
        style = MaterialTheme.typography.bodySmall,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis
    )
}