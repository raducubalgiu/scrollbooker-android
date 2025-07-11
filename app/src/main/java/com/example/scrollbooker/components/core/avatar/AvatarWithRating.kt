package com.example.scrollbooker.components.core.avatar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.bodyMedium

@Composable
fun AvatarWithRating(
    modifier: Modifier = Modifier,
    url: String =  "https://media.scrollbooker.ro/frizerie-1-cover.jpg",
    rating: String,
) {

    Box(modifier = modifier
        .size(80.dp),
        contentAlignment = Alignment.BottomCenter)
    {
        Avatar(
            url = url,
            size = 65.dp
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .offset(y = 15.dp)
                .background(
                    color = Color.Black.copy(alpha = 0.7f),
                    shape = RoundedCornerShape(15.dp)
                )
                .padding(horizontal = 6.dp, vertical = 8.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_star_solid),
                contentDescription = "Rating",
                tint = Primary,
                modifier = Modifier.size(16.dp)
            )
            Spacer(Modifier.width(2.dp))
            Text(
                text = rating,
                color = Color.White,
                fontWeight = FontWeight.ExtraBold,
                style = bodyMedium,
            )
        }
    }
}