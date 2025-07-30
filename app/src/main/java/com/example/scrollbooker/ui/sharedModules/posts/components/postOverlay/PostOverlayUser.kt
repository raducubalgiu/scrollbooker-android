package com.example.scrollbooker.ui.sharedModules.posts.components.postOverlay

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.avatar.AvatarWithRating
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXS
import com.example.scrollbooker.ui.theme.bodyLarge

@Composable
fun PostOverlayUser(
    fullName: String,
    profession: String,
    ratingsAverage: String,
    distance: Float?
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AvatarWithRating(
            rating = ratingsAverage,
            size = 50.dp
        )
        Spacer(Modifier.width(SpacingM))
        Column {
            Text(
                text = fullName,
                style = bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                fontSize = 18.sp
            )
            Spacer(Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = profession,
                    style = TextStyle(
                        shadow = Shadow(
                            color = Color.Black.copy(alpha = 0.6f),
                            offset = Offset(1f, 1f),
                            blurRadius = 3f
                        ),
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        letterSpacing = 0.5.sp
                    ),
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Gray.copy(alpha = 0.85f)
                )
                distance?.let {
                    Spacer(Modifier.width(SpacingM))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(R.drawable.ic_location_outline),
                            contentDescription = null,
                            tint = Color.Gray.copy(alpha = 0.85f)
                        )
                        Spacer(Modifier.width(SpacingXS))
                        Text(
                            text = "$distance km",
                            style = TextStyle(
                                shadow = Shadow(
                                    color = Color.Black.copy(alpha = 0.6f),
                                    offset = Offset(1f, 1f),
                                    blurRadius = 3f
                                ),
                                fontSize = 16.sp,
                                lineHeight = 24.sp,
                                letterSpacing = 0.5.sp
                            ),
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Gray.copy(alpha = 0.85f)
                        )
                        Spacer(Modifier.width(SpacingS))
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            tint = Color.Gray.copy(alpha = 0.85f)
                        )
                    }
                }
            }
        }
    }
}