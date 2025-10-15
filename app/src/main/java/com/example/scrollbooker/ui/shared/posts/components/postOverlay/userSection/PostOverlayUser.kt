package com.example.scrollbooker.ui.shared.posts.components.postOverlay.userSection
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.entity.social.post.domain.model.PostBusinessOwner
import com.example.scrollbooker.entity.social.post.domain.model.PostEmployee
import com.example.scrollbooker.entity.user.userSocial.domain.model.UserSocial
import com.example.scrollbooker.ui.theme.bodyLarge

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

    Column(
        modifier = Modifier
            .padding(vertical = SpacingS)
            .clickable(
                onClick = { onNavigateToUser(user.id) },
                interactionSource = interactionSource,
                indication = null
            ),
    ) {
        Text(
            text = user.fullName,
            style = bodyLarge,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
            fontSize = 18.sp
        )

        Spacer(Modifier.height(SpacingS))

        PostProfessionLabel(profession = user.profession ?: "")
    }

    when {
        isVideoReview && employee != null -> {
            PostUserSummary(
                profession = user.profession ?: "",
                fullName = employee.fullName,
                avatar = employee.avatar ?: "",
                distance = distance,
                onNavigateToUser = { onNavigateToUser(employee.id) }
            )
        }

        !isBusiness -> {
            PostUserSummary(
                profession = user.profession ?: "",
                fullName = businessOwner.fullName,
                avatar = businessOwner.avatar ?: "",
                distance = distance,
                onNavigateToUser = { onNavigateToUser(businessOwner.id) }
            )
        }

        else -> {
            Row(verticalAlignment = Alignment.CenterVertically) {
                //PostProfessionLabel(profession = user.profession ?: "")

                distance?.let {
                    Text(
                        text = "la $distance km de tine",
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