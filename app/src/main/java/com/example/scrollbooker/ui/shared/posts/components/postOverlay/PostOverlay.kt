package com.example.scrollbooker.ui.shared.posts.components.postOverlay
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.util.Dimens.SpacingXS
import com.example.scrollbooker.core.util.Dimens.SpacingXXS
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.navigation.navigators.NavigateCalendarParam
import com.example.scrollbooker.ui.shared.posts.PostActionUiState
import com.example.scrollbooker.ui.shared.posts.components.PostActionButtonSmall
import com.example.scrollbooker.ui.shared.posts.components.postOverlay.userSection.PostOverlayUser
import com.example.scrollbooker.ui.theme.BackgroundDark
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.LastMinute
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.labelLarge
import com.example.scrollbooker.ui.theme.labelMedium
import com.example.scrollbooker.ui.theme.titleMedium
import java.math.BigDecimal

@Composable
fun PostOverlay(
    post: Post,
    postActionState: PostActionUiState,
    onAction: (PostOverlayActionEnum) -> Unit,

    shouldDisplayBottomBar: Boolean,
    onShowBottomBar: (() -> Unit)? = null,
    onNavigateToUserProfile: (Int) -> Unit,
    onNavigateToCalendar: (NavigateCalendarParam) -> Unit,
    onNavigateToProducts: () -> Unit
) {
    val discount = post.product?.discount

    Column(modifier = Modifier
        .fillMaxSize()
        .zIndex(3f)
        .background(
            Brush.verticalGradient(
                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f))
            )
        ),
        verticalArrangement = Arrangement.Bottom
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(top = SpacingS, start = SpacingM),
            verticalAlignment = Alignment.Bottom
        ) {
            Column(modifier = Modifier
                .weight(1f)
                .padding(end = SpacingXL)
            ) {
                when {
                    post.isVideoReview -> {
                        Row(
                            modifier = Modifier
                                .clip(shape = ShapeDefaults.ExtraSmall)
                                .background(Color.Black.copy(alpha = 0.2f))
                                .padding(vertical = 6.dp, horizontal = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Recenzie verificata",
                                color = Color.White,
                                style = bodyLarge,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )

                            Spacer(Modifier.width(SpacingM))

                            Row(modifier = Modifier,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    modifier = Modifier.size(20.dp),
                                    painter = painterResource(R.drawable.ic_star_solid),
                                    contentDescription = null,
                                    tint = Primary
                                )

                                Spacer(Modifier.width(SpacingXS))

                                Text(
                                    text = "${post.rating}",
                                    style = labelLarge,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                )
                            }
                        }
                    }

                    post.lastMinute.isLastMinute -> {
                        PostOverlayLabel(
                            icon = R.drawable.ic_bolt_solid,
                            title = stringResource(R.string.lastMinute),
                            containerColor = Error
                        )
                    }

                    discount?.let { it > BigDecimal.ZERO } == true -> {
                        PostOverlayLabel(
                            icon = R.drawable.ic_percent_badge_solid,
                            title = stringResource(R.string.sale),
                            containerColor = Error
                        )
                    }
                }

                Spacer(Modifier.height(SpacingXS))

                PostOverlayUser(
                    user = post.user,
                    businessOwner = post.businessOwner,
                    employee = post.employee,
                    isVideoReview = post.isVideoReview,
                    distance = 5f,
                    onNavigateToUser = onNavigateToUserProfile
                )

                Spacer(Modifier.height(SpacingM))

                post.description?.takeIf { it.isNotBlank() }?.let { description ->
                    PostOverlayDescription(description)
                }

                if(!post.isVideoReview && post.product != null) {
                    PostOverlayProduct(product = post.product)
                }

                AnimatedContent(
                    targetState = shouldDisplayBottomBar,
                    transitionSpec = { fadeIn(tween(300)) togetherWith fadeOut(tween(300)) },
                    label = "HeaderTransition"
                ) { target ->
                    if(target) {
                        PostActionButtonSmall(
                            title = when {
                                post.isVideoReview -> "Serviciul rezervat"
                                else -> "Rezerva"
                            },
                            onNavigateToCalendar = {
                                post.product?.let {
                                    onNavigateToCalendar(
                                        NavigateCalendarParam(
                                            userId = post.user.id,
                                            slotDuration = post.product.duration,
                                            productId = post.product.id,
                                            productName = post.product.name
                                        )
                                    )
                                }
                            },
                            onNavigateToProducts = onNavigateToProducts
                        )
                    }
                }
            }

            PostOverlayActions(
                user = post.user,
                isVideoReview = post.isVideoReview,
                counters = post.counters,
                userActions = post.userActions,
                onAction = onAction,
                shouldDisplayBottomBar = shouldDisplayBottomBar,
                onShowBottomBar = onShowBottomBar,
                onNavigateToUser = { onNavigateToUserProfile(post.user.id) }
            )
        }
    }
}