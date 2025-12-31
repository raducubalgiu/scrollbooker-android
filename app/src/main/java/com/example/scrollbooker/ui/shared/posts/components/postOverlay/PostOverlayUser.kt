package com.example.scrollbooker.ui.shared.posts.components.postOverlay
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.avatar.Avatar
import com.example.scrollbooker.core.extensions.withAlpha
import com.example.scrollbooker.core.util.Dimens.AvatarSizeXXS
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.entity.social.post.domain.model.PostBusinessOwner
import com.example.scrollbooker.entity.social.post.domain.model.PostUser
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.bodyMedium

@Composable
fun PostOverlayUser(
    enableOpacity: Boolean = false,
    showPhone: Boolean,
    user: PostUser,
    businessOwner: PostBusinessOwner,
    isVideoReview: Boolean,
    onNavigateToUser: (Int) -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isBusiness = user.id == businessOwner.id

    if(showPhone) {
        Button(
            onClick = {},
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White.copy(alpha = 0.1f),
                contentColor = OnBackground
            ),
            contentPadding = PaddingValues(
                vertical = 10.dp,
                horizontal = BasePadding
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.size(17.dp),
                    painter = painterResource(R.drawable.ic_call_outline),
                    contentDescription = null,
                    tint = Color.White
                )

                Spacer(Modifier.width(SpacingS))

                Text(
                    text = "Sună",
                    style = bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }
    }

    Column(
        modifier = Modifier
            .padding(vertical = SpacingS)
            .clickable(
                onClick = { onNavigateToUser(user.id) },
                interactionSource = interactionSource,
                indication = null
            ),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = user.fullName,
                style = bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = Color.White.withAlpha(enableOpacity),
                fontSize = 18.sp
            )
        }

        Spacer(Modifier.height(SpacingS))

        when {
            isVideoReview -> {
                SecondaryText(
                    text = "${stringResource(R.string.hasTestedTheService)} Tuns",
                    color = Color.White.withAlpha(enableOpacity),
                    fontWeight = FontWeight.Normal,
                    fontStyle = FontStyle.Italic
                )
            }
            else -> SecondaryText(user.profession, enableOpacity)
        }

        Spacer(Modifier.height(SpacingM))
    }

    if(!isBusiness) {
        PostBusiness(
            enableOpacity = enableOpacity,
            fullName = businessOwner.fullName,
            avatar = businessOwner.avatar,
            onNavigateToUser = { onNavigateToUser(businessOwner.id) }
        )
    }
}

@Composable
private fun SecondaryText(
    text: String,
    enableOpacity: Boolean = false,
    color: Color = Primary.copy(alpha = if(enableOpacity) 0.5f else 0.85f),
    fontWeight: FontWeight = FontWeight.SemiBold,
    fontStyle: FontStyle = FontStyle.Normal
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
            letterSpacing = 0.5.sp,
        ),
        fontWeight = fontWeight,
        color = color,
        fontStyle = fontStyle
    )
}

@Composable
fun PostBusiness(
    enableOpacity: Boolean = false,
    fullName: String,
    avatar: String?,
    onNavigateToUser: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = Modifier.clickable(
            interactionSource = interactionSource,
            indication = null,
            onClick = onNavigateToUser
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Repeat,
            contentDescription = "Repeat Icon",
            tint = Primary.withAlpha(enableOpacity)
        )

        Spacer(Modifier.width(SpacingM))

        Avatar(
            url = avatar ?: "",
            size = AvatarSizeXXS
        )

        Spacer(Modifier.width(SpacingM))

        Text(
            text = fullName,
            style = bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = Color.White.withAlpha(enableOpacity),
            fontSize = 16.sp
        )
    }

    Spacer(Modifier.height(SpacingS))
}