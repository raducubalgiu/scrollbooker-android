package com.example.scrollbooker.ui.shared.reviews.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.Rating
import com.example.scrollbooker.ui.theme.bodyLarge

@Composable
fun ReviewSummaryCheckbox(
    rating: Int,
    progress: Float,
    count: Int,
    isEnabled: Boolean,
    isChecked: Boolean,
    onCheckChange: (Boolean) -> Unit
) {
    Row(modifier = Modifier.padding(
        bottom = SpacingS
    ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .size(18.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(
                    color = when {
                        !isEnabled -> Divider
                        isChecked -> Primary
                        else -> Color.Transparent
                    }
                )
                .border(
                    width = 1.dp,
                    color = when {
                        !isEnabled -> Divider
                        isChecked -> Primary
                        else -> Divider
                    },
                    shape = RoundedCornerShape(4.dp)
                )
                .clickable(
                    onClick = {  onCheckChange(!isChecked) },
                    enabled = isEnabled
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isChecked) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(12.dp)
                )
            }
        }

        Text(
            modifier = Modifier
                .padding(start = 10.dp)
                .width(16.dp),
            text = "$rating",
            textAlign = TextAlign.End,
            style = bodyLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.width(BasePadding))

        Box(modifier = Modifier.weight(1f)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(5.dp)
                    .background(Color.Gray.copy(alpha = 0.4f))
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction = progress)
                    .height(5.dp)
                    .background(Rating)
            )
        }

        Spacer(Modifier.width(8.dp))

        Text(
            modifier = Modifier.width(30.dp),
            text = "$count",
            textAlign = TextAlign.End,
            style = bodyLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.width(BasePadding))
    }
}