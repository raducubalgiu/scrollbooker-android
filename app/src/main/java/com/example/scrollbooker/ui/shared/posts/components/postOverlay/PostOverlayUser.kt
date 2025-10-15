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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.components.core.avatar.Avatar
import com.example.scrollbooker.core.util.Dimens.AvatarSizeXXS
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.entity.social.post.domain.model.PostBusinessOwner
import com.example.scrollbooker.entity.social.post.domain.model.PostEmployee
import com.example.scrollbooker.entity.user.userSocial.domain.model.UserSocial
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.bodyMedium

@Composable
fun PostOverlayUser(
    user: UserSocial,
    businessOwner: PostBusinessOwner,
    employee: PostEmployee?,
    isVideoReview: Boolean,
    distance: Float?,
    onNavigateToUser: (Int) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isBusiness = user.id == businessOwner.id

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Column(
                modifier = Modifier
                    .clickable(
                        onClick = { onNavigateToUser(user.id) },
                        interactionSource = interactionSource,
                        indication = null
                    )
            ) {
                Text(
                    text = user.fullName,
                    style = bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    fontSize = 18.sp
                )
            }

            when {
                isVideoReview && employee != null -> {
                   Column {
                       Spacer(Modifier.height(SpacingS))

                       Text(
                           text = user.profession ?: "",
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

                           Row(
                               modifier = Modifier.clickable(
                                   interactionSource = interactionSource,
                                   indication = null,
                                   onClick = { onNavigateToUser(employee.id) }
                               ),
                               verticalAlignment = Alignment.CenterVertically
                           ) {
                               Avatar(
                                   url = employee.avatar ?: "",
                                   size = AvatarSizeXXS
                               )

                               Spacer(Modifier.width(SpacingM))

                               Text(
                                   text = employee.fullName,
                                   style = bodyMedium,
                                   fontWeight = FontWeight.SemiBold,
                                   color = Color.White,
                                   fontSize = 16.sp
                               )
                           }

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
                   }
                }

                !isBusiness -> {
                    Column {
                        Spacer(Modifier.height(SpacingS))

                        Text(
                            text = user.profession ?: "",
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

                            Row(
                                modifier = Modifier.clickable(
                                    interactionSource = interactionSource,
                                    indication = null,
                                    onClick = { onNavigateToUser(businessOwner.id) }
                                ),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Avatar(
                                    url = businessOwner.avatar ?: "",
                                    size = AvatarSizeXXS
                                )

                                Spacer(Modifier.width(SpacingM))

                                Text(
                                    text = businessOwner.fullName,
                                    style = bodyMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White,
                                    fontSize = 16.sp
                                )
                            }

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
                    }
                }

                else -> {
                    Column {
                        Spacer(Modifier.height(SpacingS))

                        Row {
                            Text(
                                text = user.profession ?: "",
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
                    }
                }
            }
        }
    }
}