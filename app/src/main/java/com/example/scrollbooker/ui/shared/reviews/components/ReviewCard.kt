package com.example.scrollbooker.ui.shared.reviews.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.example.scrollbooker.core.util.Dimens.AvatarSizeXXS
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.util.Dimens.SpacingXS
import com.example.scrollbooker.entity.booking.review.data.remote.ReviewLabel
import com.example.scrollbooker.entity.booking.review.domain.model.Review
import com.example.scrollbooker.ui.shared.reviews.ReviewActionUiState
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.bodyLarge
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
    val createdAt = "${
        OffsetDateTime.parse(review.createdAt, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
            .format(DateTimeFormatter.ofPattern("dd MM yyyy, HH:mm"))
    }"

    val animatedScale by animateFloatAsState(
        targetValue = scale,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "iconScale"
    )

    Column(modifier = Modifier
        .padding(BasePadding)
        .clip(ShapeDefaults.Medium)
        .background(SurfaceBG)
        .clickable { onNavigateToReviewDetail() }
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(SpacingM)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Avatar(url = review.customer.avatar ?: "")
                Spacer(Modifier.width(SpacingM))
                Column {
                    Text(
                        text = review.customer.fullName,
                        style = bodyLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        color = Color.Gray,
                        text = createdAt
                    )
                }
            }

            Text(
                modifier = Modifier.padding(top = BasePadding),
                text = stringResource(id = ReviewLabel.fromValue(review.rating).labelRes),
                style = titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            RatingsStars(
                starSize = 20.dp,
                modifier = Modifier.padding(
                    vertical = SpacingS
                ),
                rating = review.rating.toFloat()
            )

            if(review.review.isNotEmpty() == true) {
                Text(text = review.review)

                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = SpacingXL),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if(reviewUi.isLikedByProductOwner) {
                            Avatar(
                                url = review.productBusinessOwner.avatar ?: "",
                                size = AvatarSizeXXS
                            )
                            Spacer(Modifier.width(BasePadding))
                        }

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
                                    style = bodyLarge,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if(reviewUi.isLiked) Error else Color.Gray
                                )
                            }

                            Spacer(Modifier.width(SpacingXS))

                            Icon(
                                modifier = Modifier.scale(animatedScale),
                                imageVector = if(reviewUi.isLiked) Icons.Default.Favorite
                                              else Icons.Default.FavoriteBorder,
                                contentDescription = null,
                                tint = if(reviewUi.isLiked) Error else Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }
}