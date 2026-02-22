package com.example.scrollbooker.components.core.avatar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.core.extensions.toFixedDecimals
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.bodyMedium

@Composable
fun AvatarWithRating(
    modifier: Modifier = Modifier,
    url: String,
    rating: Float,
    size: Dp = 75.dp,
    elevation: Dp = 1.dp,
    onClick: () -> Unit
) {
    val isSystemInDarkMode = isSystemInDarkTheme()

    Box(
        modifier = modifier
            .padding(bottom = 10.dp)
            .clickable(
                onClick = onClick,
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ),
        contentAlignment = Alignment.BottomCenter
    ) {
        Avatar(
            url = url,
            size = size,
            onClick = null
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .offset(y = 15.dp)
                .shadow(
                    elevation = elevation,
                    shape = CircleShape,
                    clip = false
                )
                .background(
                    color = if(isSystemInDarkMode) SurfaceBG else Background,
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
                text = rating.toFixedDecimals(1),
                color = if(isSystemInDarkMode) OnSurfaceBG else OnBackground,
                fontWeight = FontWeight.ExtraBold,
                style = bodyMedium
            )
        }
    }
}
