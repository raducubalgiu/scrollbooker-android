package com.example.scrollbooker.components.customized

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.R

@Composable
fun RatingsStars(
    rating: Float,
    modifier: Modifier = Modifier,
    maxRating: Int = 5,
    starSize: Dp = 24.dp,
    tint: Color = Primary
) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        for (i in 1..maxRating) {
            Icon(
                painter = painterResource(id =
                    if(i <= rating) R.drawable.ic_star_solid
                    else R.drawable.ic_star_outline),
                contentDescription = null,
                modifier = Modifier.size(starSize),
                tint = tint
            )
        }
    }
}