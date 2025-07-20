package com.example.scrollbooker.components.core.avatar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.scrollbooker.R
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.bodyMedium

@Composable
fun AvatarWithRating(
    modifier: Modifier = Modifier,
    url: String =  "https://media.scrollbooker.ro/frizerie-1-cover.jpg",
    rating: String,
    size: Dp = 75.dp
) {

    Box(modifier = modifier
        .padding(bottom = 10.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        AsyncImage(
            model = url,
            contentDescription = "User Avatar",
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .border(1.dp, Divider, CircleShape),
            placeholder = painterResource(R.drawable.ic_user),
            error = painterResource(R.drawable.ic_user),
            contentScale = ContentScale.Crop,
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .offset(y = 15.dp)
                .shadow(
                    elevation = 1.dp,
                    shape = CircleShape,
                    clip = false
                )
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(15.dp)
                )
                .padding(horizontal = 8.dp, vertical = 6.dp)
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
                color = Color.Black,
                fontWeight = FontWeight.ExtraBold,
                style = bodyMedium,
            )
        }
    }
}