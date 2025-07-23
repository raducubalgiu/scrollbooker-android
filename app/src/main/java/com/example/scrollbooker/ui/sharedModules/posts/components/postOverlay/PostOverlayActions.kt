package com.example.scrollbooker.ui.sharedModules.posts.components.postOverlay

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.scrollbooker.R
import com.example.scrollbooker.ui.sharedModules.posts.PostInteractionState
import com.example.scrollbooker.ui.sharedModules.posts.components.PostActionButton
import com.example.scrollbooker.ui.theme.Error

@Composable
fun PostOverlayActions(
    interactionState: PostInteractionState,
    onAction: (PostOverlayActionEnum) -> Unit,
    commentCount: Int,
    shareCount: Int
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
            onClick = { onAction(PostOverlayActionEnum.SHARE) }
        )
    }
}