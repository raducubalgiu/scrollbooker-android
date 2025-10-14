package com.example.scrollbooker.ui.modules.posts.components.postOverlay
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.zIndex
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.navigation.navigators.NavigateCalendarParam
import com.example.scrollbooker.ui.feed.PostActionUiState
import com.example.scrollbooker.ui.modules.posts.PostInteractionState
import com.example.scrollbooker.ui.modules.posts.components.PostActionButtonSmall
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.LastMinute
import java.math.BigDecimal

@Composable
fun PostOverlay(
    post: Post,
    postActionState: PostActionUiState,
    onAction: (PostOverlayActionEnum) -> Unit,
    shouldDisplayBottomBar: Boolean,
    onShowBottomBar: () -> Unit,
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
            .padding(top = SpacingS, start = SpacingS),
            verticalAlignment = Alignment.Bottom
        ) {
            Column(modifier = Modifier
                .weight(1f)
                .padding(end = SpacingXL)
            ) {
                when {
                    post.lastMinute.isLastMinute -> {
                        PostOverlayLabel(
                            icon = R.drawable.ic_bolt_solid,
                            title = stringResource(R.string.lastMinute),
                            containerColor = LastMinute
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

                Spacer(Modifier.height(SpacingM))

                PostOverlayUser(
                    fullName = post.user.fullName,
                    profession = post.user.profession ?: "",
                    ratingsAverage = "4.5",
                    distance = 5f,
                    onNavigateToUser = { onNavigateToUserProfile(post.user.id) }
                )

                Spacer(Modifier.height(SpacingM))

                post.description?.takeIf { it.isNotBlank() }?.let { description ->
                    PostOverlayDescription(description)
                }

                post.product?.let {
                    PostOverlayProduct(product = post.product)
                }

                AnimatedContent(
                    targetState = shouldDisplayBottomBar,
                    transitionSpec = { fadeIn(tween(300)) togetherWith fadeOut(tween(300)) },
                    label = "HeaderTransition"
                ) { target ->
                    if(target) {
                        PostActionButtonSmall(
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