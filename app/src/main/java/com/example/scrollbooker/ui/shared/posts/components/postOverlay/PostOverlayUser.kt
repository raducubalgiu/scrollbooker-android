package com.example.scrollbooker.ui.shared.posts.components.postOverlay
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.entity.social.post.domain.model.PostBusinessOwner
import com.example.scrollbooker.entity.user.userSocial.domain.model.UserSocial
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.bodyLarge

@Composable
fun PostOverlayUser(
    user: UserSocial,
    businessOwner: PostBusinessOwner,
    isVideoReview: Boolean,
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

        when {
            isVideoReview -> {
                SecondaryText(
                    text = "a evaluat serviciul de Tuns",
                    color = Color.White
                )
            }
            else -> SecondaryText(user.profession ?: "")
        }

        Spacer(Modifier.height(SpacingM))
    }

    if(!isBusiness) {
        PostBusiness(
            fullName = businessOwner.fullName,
            avatar = businessOwner.avatar,
            onNavigateToUser = {}
        )
    }
}

@Composable
private fun SecondaryText(
    text: String,
    color: Color = Primary.copy(alpha = 0.85f)
) {
    Text(
        text = text,
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
        color = color
    )
}