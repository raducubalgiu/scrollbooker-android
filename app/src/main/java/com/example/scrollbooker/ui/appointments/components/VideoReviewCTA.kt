package com.example.scrollbooker.ui.appointments.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.R
import com.example.scrollbooker.core.extensions.toDecimals
import com.example.scrollbooker.core.extensions.toTwoDecimals
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.OnPrimary
import com.example.scrollbooker.ui.theme.bodySmall
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun VideoReviewCTA(
    modifier: Modifier = Modifier,
    onNavigateToCamera: () -> Unit,
    discount: Float = 10f
) {
    val infinite = rememberInfiniteTransition(label = "video_cta")
    val pulseScale by infinite.animateFloat(
        initialValue = 1f, targetValue = 1.03f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )
    val iconScale by infinite.animateFloat(
        initialValue = 1f, targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "iconScale"
    )
    val haloAlpha by infinite.animateFloat(
        initialValue = 0.12f, targetValue = 0.22f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "haloAlpha"
    )

    val isSystemInDarkMode = isSystemInDarkTheme()

    val container = if (!isSystemInDarkMode) Color(0xFF141414) else Color(0xFFFF4D4D)
    val containerHigh = if (!isSystemInDarkMode) Color(0xFF141414) else Color(0xFFFF4D4D)
    val textColor = OnPrimary

    val promoRed = if (isSystemInDarkMode) OnPrimary else Error
    val videoIcon = if (isSystemInDarkMode) R.drawable.ic_video_outline else R.drawable.ic_video_solid

    val subtitleAnnotated = buildAnnotatedString {
        append("${stringResource(R.string.youGet)} ")

        withStyle(
            style = SpanStyle(
                color = promoRed,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp
            )
        ) {
            append("${discount.toDecimals()}%")
        }

        append(" ${stringResource(R.string.discountForTheNextBooking)}")
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer { scaleX = pulseScale; scaleY = pulseScale }
            .clickable(
                onClick = onNavigateToCamera
            ),
        color = container,
        shape = ShapeDefaults.ExtraLarge,
        tonalElevation = 4.dp,
        shadowElevation = 8.dp,
        border = BorderStroke(
            1.dp,
            Brush.horizontalGradient(
                listOf(containerHigh.copy(alpha = 0.6f), container.copy(alpha = 0.2f))
            )
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color.White.copy(alpha = haloAlpha),
                            Color.Transparent
                        ),
                        radius = 380f,
                        center = Offset(120f, 60f)
                    )
                )
                .padding(horizontal = 18.dp, vertical = 14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp, Alignment.Start)
            ) {

                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .graphicsLayer { scaleX = iconScale; scaleY = iconScale }
                        .background(color = textColor.copy(alpha = 0.16f), shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(videoIcon),
                        contentDescription = null,
                        tint = textColor,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Column(Modifier.weight(1f)) {
                    Text(
                        text = stringResource(R.string.addVideoReview),
                        color = textColor,
                        style = titleMedium.copy(fontWeight = FontWeight.SemiBold),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = subtitleAnnotated,
                        color = textColor.copy(alpha = 0.9f),
                        style = bodySmall,
                        maxLines = 2
                    )
                }

                Icon(
                    imageVector = Icons.Outlined.ChevronRight,
                    contentDescription = null,
                    tint = textColor.copy(alpha = 0.9f)
                )
            }
        }
    }
}