package com.example.scrollbooker.ui.shared.posts.components.postOverlay
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
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
import com.example.scrollbooker.core.util.Dimens.AvatarSizeXXS
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.entity.social.post.domain.model.PostBusinessOwner
import com.example.scrollbooker.entity.social.post.domain.model.PostUser
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.bodyMedium

@Composable
fun PostOverlayUser(
    user: PostUser,
    businessOwner: PostBusinessOwner,
    isVideoReview: Boolean,
    onNavigateToUser: (Int) -> Unit,
    onOpenReviews: () -> Unit,
    onOpenLocation: () -> Unit
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
                    text = "${stringResource(R.string.hasTestedTheService)} Tuns",
                    color = Color.White,
                    fontWeight = FontWeight.Normal,
                    fontStyle = FontStyle.Italic
                )
            }
            else -> SecondaryText(user.profession)
        }

        Spacer(Modifier.height(SpacingM))
    }

    if(!isBusiness) {
        PostBusiness(
            fullName = businessOwner.fullName,
            avatar = businessOwner.avatar,
            rating = businessOwner.ratingsAverage,
            showRating = isVideoReview,
            onNavigateToUser = { onNavigateToUser(businessOwner.id) },
            onOpenReviews = onOpenReviews,
            onOpenLocation = onOpenLocation
        )
    }
}

@Composable
private fun SecondaryText(
    text: String,
    color: Color = Primary.copy(alpha = 0.85f),
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
    fullName: String,
    avatar: String?,
    rating: Float,
    showRating: Boolean = false,
    onNavigateToUser: () -> Unit,
    onOpenReviews: () -> Unit,
    onOpenLocation: () -> Unit
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
            tint = Primary
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
            color = Color.White,
            fontSize = 16.sp
        )

        Spacer(Modifier.width(SpacingM))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
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
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = { if(showRating) onOpenReviews() else onOpenLocation() }
                )
        ) {
            Icon(
                painter = if(showRating) painterResource(R.drawable.ic_star_solid)
                          else painterResource(R.drawable.ic_location_outline),
                contentDescription = "Rating",
                tint = Primary,
                modifier = Modifier.size(16.dp)
            )
            Spacer(Modifier.width(2.dp))
            Text(
                text = if(showRating) rating.toString() else stringResource(R.string.location),
                color = Color.Black,
                fontWeight = FontWeight.ExtraBold,
                style = bodyMedium,
            )
        }
    }

    Spacer(Modifier.height(SpacingS))
}