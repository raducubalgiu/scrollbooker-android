package com.example.scrollbooker.ui.shared.reviews.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.components.core.avatar.Avatar
import com.example.scrollbooker.components.customized.RatingsStars
import com.example.scrollbooker.core.util.Dimens.AvatarSizeM
import com.example.scrollbooker.core.util.Dimens.AvatarSizeXXS
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXS
import com.example.scrollbooker.entity.booking.review.data.remote.ReviewLabel
import com.example.scrollbooker.entity.booking.review.domain.model.Review
import com.example.scrollbooker.ui.shared.reviews.ReviewActionUiState
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.bodyMedium
import com.example.scrollbooker.ui.theme.bodySmall
import com.example.scrollbooker.ui.theme.titleMedium
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.format.DateTimeFormatter

@Composable
fun ReviewCard(
    review: Review,
    reviewUi: ReviewActionUiState,
    onNavigateToReviewDetail: () -> Unit,
    onLike: () -> Unit
) {
    var scale by remember { mutableFloatStateOf(1f) }

    val createdAt = remember(review.createdAt) {
        OffsetDateTime.parse(review.createdAt, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
            .format(DateTimeFormatter.ofPattern("dd.MM.yyyy • HH:mm"))
    }

    val animatedScale by animateFloatAsState(
        targetValue = scale,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "iconScale"
    )


    Column(Modifier.padding(horizontal = BasePadding, vertical = BasePadding)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onNavigateToReviewDetail() },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Avatar(
                url = review.customer.avatar ?: "",
                size = AvatarSizeM
            )

            Spacer(modifier = Modifier.width(SpacingM))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = SpacingXS)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = review.customer.fullName,
                            style = bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = createdAt,
                            style = bodyMedium,
                            color = Color.Gray
                        )
                    }

                    RatingsStars(
                        starSize = 16.dp,
                        rating = review.rating.toFloat()
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(SpacingM))

        Text(
            modifier = Modifier,
            text = stringResource(id = ReviewLabel.fromValue(review.rating).labelRes),
            style = titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(SpacingM))

        if (review.review.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(ShapeDefaults.Medium)
            ) {
                Text(
                    text = review.review,
                    style = bodyLarge
                )
            }
        } else Text(
            text = "...",
            style = bodyLarge
        )

        Spacer(modifier = Modifier.height(SpacingM))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Box(modifier = Modifier.height(AvatarSizeXXS)) {
                if (reviewUi.isLikedByProductOwner) {
                    Avatar(
                        url = review.productBusinessOwner.avatar ?: "",
                        size = AvatarSizeXXS
                    )
                }
            }

            Spacer(Modifier.width(SpacingS))

            Row(
                modifier = Modifier.clickable(
                    onClick = onLike,
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AnimatedVisibility(
                    visible = reviewUi.likeCount > 0,
                    enter = fadeIn() + scaleIn(),
                    exit = fadeOut() + scaleOut()
                ) {
                    Text(
                        text = "${reviewUi.likeCount}",
                        style = bodySmall,
                        fontWeight = FontWeight.SemiBold,
                        color = if (reviewUi.isLiked) Error else Color.Gray
                    )
                }

                Spacer(Modifier.width(SpacingXS))

                Icon(
                    modifier = Modifier
                        .size(18.dp)
                        .scale(animatedScale),
                    imageVector = if (reviewUi.isLiked) Icons.Default.Favorite
                    else Icons.Default.FavoriteBorder,
                    contentDescription = null,
                    tint = if (reviewUi.isLiked) Error else Color.Gray
                )
            }
        }
    }
}
