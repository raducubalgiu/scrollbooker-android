package com.example.scrollbooker.screens.feed.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ModeComment
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.shared.post.domain.model.PostCounters
import com.example.scrollbooker.shared.post.domain.model.UserPostActions

@Composable
fun PostOverlay(
    userActions: UserPostActions,
    counters: PostCounters
) {
    Box(modifier = Modifier
        .fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.weight(1f)) {}

            Column {
                PostActionButton(
                    isAction = userActions.isLiked,
                    counter = counters.likeCount,
                    icon = Icons.Default.Favorite,
                )
                PostActionButton(
                    isAction = false,
                    counter = counters.commentCount,
                    icon = Icons.Default.ModeComment,
                )
                PostActionButton(
                    isAction = userActions.isBookmarked,
                    counter = counters.bookmarkCount,
                    icon = Icons.Default.Bookmark,
                )
                PostActionButton(
                    isAction = false,
                    counter = counters.shareCount,
                    icon = Icons.Default.Share,
                )
            }
        }
    }
}