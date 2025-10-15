package com.example.scrollbooker.ui.shared.posts.components.postOverlay
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import com.example.scrollbooker.components.core.avatar.Avatar
import com.example.scrollbooker.core.util.Dimens.AvatarSizeXXS
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXS
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.bodyMedium

@Composable
fun PostOverlayUser(
    fullName: String,
    profession: String,
    ratingsAverage: String,
    distance: Float?,
    onNavigateToUser: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Column(
                modifier = Modifier
                    .clickable(
                        onClick = onNavigateToUser,
                        interactionSource = interactionSource,
                        indication = null
                    )
            ) {
                Text(
                    text = fullName,
                    style = bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    fontSize = 18.sp
                )
            }

            Spacer(Modifier.height(SpacingS))

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
                color = Primary.copy(alpha = 0.85f)
            )

            Spacer(Modifier.height(SpacingS))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Repeat,
                    contentDescription = "Repeat Icon",
                    tint = Primary
                )
                Spacer(Modifier.width(SpacingM))
                Avatar(
                    url = "",
                    size = AvatarSizeXXS
                )
                Spacer(Modifier.width(SpacingM))
                Text(
                    text = "Frizeria Figaro",
                    style = bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    fontSize = 16.sp
                )
                Spacer(Modifier.width(SpacingS))
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
                    color = Color.White.copy(alpha = 0.85f)
                )
                Spacer(Modifier.width(SpacingS))
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.85f),
                )
            }

//            Row(verticalAlignment = Alignment.CenterVertically) {
//                Text(
//                    text = profession,
//                    style = TextStyle(
//                        shadow = Shadow(
//                            color = Color.Black.copy(alpha = 0.6f),
//                            offset = Offset(1f, 1f),
//                            blurRadius = 3f
//                        ),
//                        fontSize = 16.sp,
//                        lineHeight = 24.sp,
//                        letterSpacing = 0.5.sp
//                    ),
//                    fontWeight = FontWeight.SemiBold,
//                    color = Primary.copy(alpha = 0.85f)
//                )
//                distance?.let {
//                    Spacer(Modifier.width(SpacingM))
//                    Row(verticalAlignment = Alignment.CenterVertically) {
//                        Icon(
//                            painter = painterResource(R.drawable.ic_location_outline),
//                            contentDescription = null,
//                            tint = Color.White.copy(alpha = 0.85f)
//                        )
//                        Spacer(Modifier.width(SpacingXS))
//                        Text(
//                            text = "$distance km",
//                            style = TextStyle(
//                                shadow = Shadow(
//                                    color = Color.Black.copy(alpha = 0.6f),
//                                    offset = Offset(1f, 1f),
//                                    blurRadius = 3f
//                                ),
//                                fontSize = 16.sp,
//                                lineHeight = 24.sp,
//                                letterSpacing = 0.5.sp
//                            ),
//                            fontWeight = FontWeight.SemiBold,
//                            color = Color.White.copy(alpha = 0.85f)
//                        )
//                        Spacer(Modifier.width(SpacingS))
//                        Icon(
//                            imageVector = Icons.Default.KeyboardArrowDown,
//                            contentDescription = null,
//                            tint = Color.White.copy(alpha = 0.85f),
//                        )
//                    }
//                }
//            }
        }
    }
}