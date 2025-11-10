package com.example.scrollbooker.ui.appointments.sheets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun AddReviewRatingSection(
    selectedRating: Int?,
    onRatingClick: (Int) -> Unit,
    ratingLabel: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(5) { index ->
            val rating = index + 1
            val fill = selectedRating?.let { rating <= it } == true

            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clickable { onRatingClick(rating) },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(37.5.dp),
                    imageVector = if(fill) Icons.Default.Star else Icons.Outlined.StarOutline,
                    contentDescription = null,
                    tint = if(fill) Primary else Divider
                )
            }
        }
    }

    Spacer(Modifier.height(SpacingM))

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "$selectedRating ${stringResource(R.string.from5)}",
            color = Color.Gray
        )

        Text(
            modifier = Modifier.padding(horizontal = SpacingM),
            text = "•"
        )

        Text(
            text = ratingLabel,
            style = titleMedium,
            fontWeight = FontWeight.SemiBold

        )
    }
}