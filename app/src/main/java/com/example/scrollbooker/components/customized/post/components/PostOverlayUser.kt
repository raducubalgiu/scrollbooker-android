package com.example.scrollbooker.components.customized.post.components
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.entity.social.post.domain.model.PostBusinessOwner
import com.example.scrollbooker.entity.social.post.domain.model.PostEmployee
import com.example.scrollbooker.entity.social.post.domain.model.PostServiceDomain
import com.example.scrollbooker.entity.social.post.domain.model.PostUser
import com.example.scrollbooker.navigation.navigators.UserProfileParam
import com.example.scrollbooker.ui.theme.Beauty
import com.example.scrollbooker.ui.theme.OnPrimary
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.bodySmall

@Composable
fun PostOverlayUser(
    user: PostUser,
    serviceDomain: PostServiceDomain?,
    isVideoReview: Boolean,
    businessOwner: PostBusinessOwner,
    employee: PostEmployee?,
    onNavigateToUser: (param: UserProfileParam) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    if(isVideoReview) {
        Surface(
            modifier = Modifier.padding(bottom = SpacingS),
            shape = ShapeDefaults.Small,
            color = Color.White.copy(alpha = 0.1f),
            contentColor = Color.White
        ) {
            Text(
                text = stringResource(R.string.videoReview),
                style = bodySmall,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(
                    vertical = 6.dp,
                    horizontal = 8.dp
                )
            )
        }
    }

    serviceDomain?.let {
        Surface(
            modifier = Modifier.padding(bottom = SpacingS),
            shape = ShapeDefaults.Small,
            color = Beauty.copy(alpha = 0.8f),
            contentColor = OnPrimary
        ) {
            Text(
                text = it.name,
                style = bodySmall,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(
                    vertical = 6.dp,
                    horizontal = 8.dp
                )
            )
        }
    }

    Column(
        modifier = Modifier
            .clickable(
                onClick = { onNavigateToUser(
                    UserProfileParam(user.id, user.username, user.profession)
                )},
                interactionSource = interactionSource,
                indication = null
            ),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = user.fullName,
                style = bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }

        Spacer(Modifier.height(SpacingS))

        when {
            isVideoReview -> {
                val reviewedId = employee?.id ?: businessOwner.id
                val reviewedUsername = employee?.username ?: businessOwner.username
                val reviewedProfession = employee?.profession ?: businessOwner.profession
                val reviewedFullName = employee?.fullName ?: businessOwner.fullName
                val reviewedInteractionSource = remember { MutableInteractionSource() }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    SecondaryText(
                        text = stringResource(R.string.leftReviewForPrefix),
                        color = Color.White,
                        fontWeight = FontWeight.Normal
                    )

                    Spacer(Modifier.width(2.dp))

                    SecondaryText(
                        text = reviewedFullName,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable(
                            interactionSource = reviewedInteractionSource,
                            indication = null,
                            onClick = {
                                onNavigateToUser(
                                    UserProfileParam(reviewedId, reviewedUsername, reviewedProfession)
                                )
                            }
                        )
                    )
                }
            }
            else -> SecondaryText(user.profession)
        }
    }
}

@Composable
private fun SecondaryText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Primary.copy(0.85f),
    fontWeight: FontWeight = FontWeight.SemiBold,
) {
    Text(
        modifier = modifier,
        text = text,
        style = TextStyle(
            shadow = Shadow(
                color = Color.Black.copy(alpha = 0.6f),
                offset = Offset(1f, 1f),
                blurRadius = 3f
            ),
            fontSize = 13.sp,
            lineHeight = 18.sp,
            letterSpacing = 0.25.sp,
        ),
        fontWeight = fontWeight,
        color = color,
    )
}
