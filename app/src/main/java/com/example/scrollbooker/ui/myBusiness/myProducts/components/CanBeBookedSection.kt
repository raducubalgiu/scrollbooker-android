package com.example.scrollbooker.ui.myBusiness.myProducts.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.ui.theme.bodySmall
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun CanBeBookedSection(
    canBeBooked: Boolean,
    onCheckChange: ((Boolean) -> Unit)?
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(R.string.productCanBeBooked),
            style = titleMedium
        )

        Checkbox(
            checked = canBeBooked,
            onCheckedChange = onCheckChange
        )
    }

    Text(
        text = stringResource(R.string.productCanBeBookedDescription),
        style = bodySmall,
        color = Color.Gray
    )
}