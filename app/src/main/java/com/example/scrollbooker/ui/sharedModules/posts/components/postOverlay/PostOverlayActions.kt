package com.example.scrollbooker.ui.sharedModules.posts.components.postOverlay
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
import androidx.compose.material3.IconButtonColors
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
import com.example.scrollbooker.ui.sharedModules.posts.PostInteractionState
import com.example.scrollbooker.ui.sharedModules.posts.components.PostActionButton
import com.example.scrollbooker.ui.theme.Error

@Composable
fun PostOverlayActions(
    interactionState: PostInteractionState,
    onAction: (PostOverlayActionEnum) -> Unit,
    commentCount: Int,
    shareCount: Int,
    shouldDisplayBottomBar: Boolean,
    onShowBottomBar: () -> Unit
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
            url = "https://media.scrollbooker.ro/avatar-men-22.jpg",
            rating = "4.5",
            size = 55.dp
        )

        Spacer(Modifier.height(SpacingS))

        PostActionButton(
            isEnabled = !interactionState.isLiking,
            counter = interactionState.likeCount,
            icon = painterResource(R.drawable.ic_heart_solid),
            tint = if (interactionState.isLiked) Error else Color.White,
            onClick = { onAction(PostOverlayActionEnum.LIKE) }
        )

        PostActionButton(
            counter = 120,
            icon = painterResource(R.drawable.ic_clipboard_check_solid),
            tint = Color.White,
            onClick = { onAction(PostOverlayActionEnum.OPEN_REVIEWS) }
        )

        PostActionButton(
            counter = commentCount,
            icon = painterResource(R.drawable.ic_comment_solid),
            tint = Color.White,
            onClick = { onAction(PostOverlayActionEnum.OPEN_COMMENTS) }
        )
        PostActionButton(
            isEnabled = !interactionState.isBookmarking,
            counter = interactionState.bookmarkCount,
            icon = painterResource(R.drawable.ic_bookmark_solid),
            tint = if (interactionState.isBookmarked) Color(0xFFF3BA2F) else Color.White,
            onClick = { onAction(PostOverlayActionEnum.BOOKMARK) }
        )
        PostActionButton(
            counter = shareCount,
            icon = painterResource(R.drawable.ic_send_solid),
            tint = Color.White,
            onClick = { onAction(PostOverlayActionEnum.SHARE) }
        )

        IconButton(
            modifier = Modifier.padding(vertical = BasePadding),
            onClick = onShowBottomBar,
            colors = IconButtonColors(
                contentColor = Color.White,
                containerColor = Color.Black.copy(alpha = 0.5f),
                disabledContainerColor = Color.Black.copy(alpha = 0.7f),
                disabledContentColor = Color.White
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