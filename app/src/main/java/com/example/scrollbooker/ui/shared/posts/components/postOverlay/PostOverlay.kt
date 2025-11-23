package com.example.scrollbooker.ui.shared.posts.components.postOverlay
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
import com.example.scrollbooker.core.util.Dimens.SpacingXS
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.entity.social.post.domain.model.ctaAction
import com.example.scrollbooker.entity.social.post.domain.model.ctaTitle
import com.example.scrollbooker.ui.shared.posts.PostActionUiState
import com.example.scrollbooker.ui.shared.posts.components.PostActionButtonSmall
import com.example.scrollbooker.ui.shared.posts.components.postOverlay.labels.PostOverlayLabel
import com.example.scrollbooker.ui.shared.posts.components.postOverlay.labels.VideoReviewLabel
import com.example.scrollbooker.ui.theme.Error
import java.math.BigDecimal

@Composable
fun PostOverlay(
    post: Post,
    postActionState: PostActionUiState,
    onAction: (PostOverlayActionEnum) -> Unit,

    showBottomBar: Boolean,
    onShowBottomBar: (() -> Unit)? = null,
    onNavigateToUserProfile: (Int) -> Unit,
) {
    //val discount = post.product?.discount
    val isVideoReview = post.isVideoReview
    //val hasProduct = post.product != null
    //val hasDiscount = discount?.let { it > BigDecimal.ZERO } == true
    val isLastMinute = post.lastMinute.isLastMinute

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
//                when {
//                    isVideoReview -> VideoReviewLabel()
//                    isLastMinute -> {
//                        PostOverlayLabel(
//                            icon = R.drawable.ic_bolt_solid,
//                            title = stringResource(R.string.lastMinute),
//                            containerColor = Error
//                        )
//                    }
//
//                    hasDiscount -> {
//                        PostOverlayLabel(
//                            icon = R.drawable.ic_percent_badge_solid,
//                            title = stringResource(R.string.sale),
//                            containerColor = Error
//                        )
//                    }
//                }

                Spacer(Modifier.height(SpacingXS))

                PostOverlayUser(
                    user = post.user,
                    businessOwner = post.businessOwner,
                    isVideoReview = isVideoReview,
                    onNavigateToUser = onNavigateToUserProfile,
                    onOpenReviews = { onAction(PostOverlayActionEnum.OPEN_REVIEWS) },
                    onOpenLocation = { onAction(PostOverlayActionEnum.OPEN_LOCATION) }
                )

                Spacer(Modifier.height(SpacingS))

                post.description
                    ?.takeIf { it.isNotBlank() }
                    ?.let { PostOverlayDescription(it) }

//                if(!isVideoReview && hasProduct) {
//                    PostOverlayProduct(product = post.product)
//                }

                PostActionButtonSmall(
                    show = showBottomBar,
                    title = stringResource(post.ctaTitle()),
                    onClick = { onAction(post.ctaAction()) }
                )
            }

            PostOverlayActions(
                user = post.user,
                postActionState = postActionState,
                isVideoReview = post.isVideoReview,
                counters = post.counters,
                userActions = post.userActions,
                onAction = onAction,
                showBottomBar = showBottomBar,
                onShowBottomBar = onShowBottomBar,
                onNavigateToUser = { onNavigateToUserProfile(post.user.id) },
            )
        }
    }
}