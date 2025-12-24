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
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.util.Dimens.SpacingXS
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.entity.social.post.domain.model.ctaAction
import com.example.scrollbooker.entity.social.post.domain.model.ctaTitle
import com.example.scrollbooker.ui.shared.posts.PostActionUiState
import com.example.scrollbooker.ui.shared.posts.components.PostActionButtonSmall

@Composable
fun PostOverlay(
    post: Post,
    postActionState: PostActionUiState,
    onAction: (PostOverlayActionEnum) -> Unit,

    enableOpacity: Boolean = false,
    showBottomBar: Boolean,
    onShowBottomBar: (() -> Unit)? = null,
    onNavigateToUserProfile: (Int) -> Unit,
) {
    val isVideoReview = post.isVideoReview

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
                Spacer(Modifier.height(SpacingXS))

                PostOverlayUser(
                    enableOpacity = enableOpacity,
                    user = post.user,
                    businessOwner = post.businessOwner,
                    isVideoReview = isVideoReview,
                    onNavigateToUser = onNavigateToUserProfile
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
                enableOpacity = enableOpacity,
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