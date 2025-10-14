package com.example.scrollbooker.ui.modules.posts.components.postOverlay
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.avatar.AvatarWithRating
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.entity.social.post.domain.model.PostCounters
import com.example.scrollbooker.entity.social.post.domain.model.UserPostActions
import com.example.scrollbooker.entity.user.userSocial.domain.model.UserSocial
import com.example.scrollbooker.ui.modules.posts.components.PostActionButton
import com.example.scrollbooker.ui.theme.Error

@Composable
fun PostOverlayActions(
    user: UserSocial,
    counters: PostCounters,
    userActions: UserPostActions,
    onAction: (PostOverlayActionEnum) -> Unit,
    shouldDisplayBottomBar: Boolean,
    onShowBottomBar: () -> Unit,
    onNavigateToUser: () -> Unit
) {
    val rotation by animateFloatAsState(
        targetValue = if(shouldDisplayBottomBar) 180f else 0f,
        label = "ArrowRotation"
    )

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AvatarWithRating(
            url = user.avatar ?: "",
            rating = user.ratingsAverage,
            size = 55.dp,
            onClick = onNavigateToUser
        )

        Spacer(Modifier.height(SpacingS))

        PostActionButton(
            //isEnabled = !interactionState.isLiking,
            counter = counters.likeCount,
            icon = painterResource(R.drawable.ic_heart_solid),
            tint = if (userActions.isLiked) Error else Color.White,
            onClick = {
                onAction(PostOverlayActionEnum.LIKE)
            }
        )

        PostActionButton(
            counter = 120,
            icon = painterResource(R.drawable.ic_clipboard_check_solid),
            tint = Color.White,
            onClick = {
                //onAction(PostOverlayActionEnum.OPEN_REVIEWS)
            }
        )

        PostActionButton(
            counter = counters.commentCount,
            icon = painterResource(R.drawable.ic_comment_solid),
            tint = Color.White,
            onClick = {
                //onAction(PostOverlayActionEnum.OPEN_COMMENTS)
            }
        )
        PostActionButton(
            //isEnabled = !interactionState.isBookmarking,
            counter = counters.bookmarkCount,
            icon = painterResource(R.drawable.ic_bookmark_solid),
            tint = if (userActions.isBookmarked) Color(0xFFF3BA2F) else Color.White,
            onClick = { onAction(PostOverlayActionEnum.BOOKMARK) }
        )
        PostActionButton(
            counter = counters.repostCount,
            icon = painterResource(R.drawable.ic_send_solid),
            tint = Color.White,
            onClick = { onAction(PostOverlayActionEnum.REPOST) }
        )

        IconButton(
            modifier = Modifier.padding(vertical = BasePadding),
            onClick = onShowBottomBar,
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = Color.Black.copy(alpha = 0.5f),
                contentColor = Color.Black.copy(alpha = 0.5f)
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