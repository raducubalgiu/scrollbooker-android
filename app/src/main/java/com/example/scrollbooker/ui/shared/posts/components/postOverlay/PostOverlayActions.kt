package com.example.scrollbooker.ui.shared.posts.components.postOverlay
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardDoubleArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.avatar.Avatar
import com.example.scrollbooker.components.core.avatar.AvatarWithRating
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.entity.social.post.domain.model.PostCounters
import com.example.scrollbooker.entity.social.post.domain.model.PostUser
import com.example.scrollbooker.entity.social.post.domain.model.UserPostActions
import com.example.scrollbooker.ui.shared.posts.PostActionUiState
import com.example.scrollbooker.ui.shared.posts.components.PostActionButton
import com.example.scrollbooker.ui.theme.Error

@Composable
fun PostOverlayActions(
    user: PostUser,
    postActionState: PostActionUiState,
    isVideoReview: Boolean,
    counters: PostCounters,
    userActions: UserPostActions,
    onAction: (PostOverlayActionEnum) -> Unit,
    shouldDisplayBottomBar: Boolean,
    onShowBottomBar: (() -> Unit)? = null,
    onNavigateToUser: () -> Unit,
) {
    val rotation by animateFloatAsState(
        targetValue = if(shouldDisplayBottomBar) 180f else 0f,
        label = "ArrowRotation"
    )

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if(isVideoReview) {
            Avatar(
                url = user.avatar ?: "",
                size = 55.dp,
                onClick = onNavigateToUser
            )
        } else {
            AvatarWithRating(
                url = user.avatar ?: "",
                rating = user.ratingsAverage,
                size = 55.dp,
                onClick = onNavigateToUser
            )
        }

        Spacer(Modifier.height(SpacingS))

        PostActionButton(
            isEnabled = !postActionState.isSavingLike,
            counter = counters.likeCount,
            icon = R.drawable.ic_heart_solid,
            tint = if (userActions.isLiked) Error else Color.White,
            onClick = {
                onAction(PostOverlayActionEnum.LIKE)
            }
        )

        if(!isVideoReview) {
            PostActionButton(
                counter = user.ratingsCount,
                icon = R.drawable.ic_clipboard_check_solid,
                tint = Color.White,
                onClick = {
                    onAction(PostOverlayActionEnum.OPEN_REVIEWS)
                }
            )
        }

        PostActionButton(
            counter = counters.commentCount,
            icon = R.drawable.ic_comment_solid,
            tint = Color.White,
            onClick = {
                onAction(PostOverlayActionEnum.OPEN_COMMENTS)
            }
        )

        PostActionButton(
            isEnabled = !postActionState.isSavingBookmark,
            counter = counters.bookmarkCount,
            icon = R.drawable.ic_bookmark_solid,
            tint = if (userActions.isBookmarked) Color(0xFFF3BA2F) else Color.White,
            onClick = { onAction(PostOverlayActionEnum.BOOKMARK) }
        )

        PostActionButton(
            icon = R.drawable.ic_elipsis_horizontal,
            tint = Color.White,
            onClick = { onAction(PostOverlayActionEnum.OPEN_MORE_OPTIONS) }
        )

        onShowBottomBar?.let {
            IconButton(
                modifier = Modifier.padding(vertical = BasePadding),
                onClick = onShowBottomBar,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color.Black.copy(alpha = 0.5f),
                    contentColor = Color.White
                )
            ) {
                Icon(
                    modifier = Modifier.rotate(rotation),
                    imageVector = Icons.Default.KeyboardDoubleArrowUp,
                    contentDescription = null
                )
            }
        }
    }
}