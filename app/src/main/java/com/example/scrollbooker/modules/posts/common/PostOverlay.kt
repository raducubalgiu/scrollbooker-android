package com.example.scrollbooker.modules.posts.common
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.avatar.AvatarWithRating
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.entity.post.domain.model.PostCounters
import com.example.scrollbooker.modules.posts.PostInteractionState
import com.example.scrollbooker.ui.theme.Error

@Composable
fun PostOverlay(
    interactionState: PostInteractionState,
    counters: PostCounters,
    onLike: () -> Unit,
    onBookmark: () -> Unit,
    onOpenReviews: () -> Unit,
    onOpenComments: () -> Unit,
    onOpenCalendar: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = BasePadding),
            verticalAlignment = Alignment.Bottom
        ) {
            Column(
                modifier = Modifier
                    .padding(start = BasePadding)
                    .weight(1f)
            ) {
                Text("Hello World")
            }

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AvatarWithRating(
                    rating = "4.5"
                )

                Spacer(Modifier.height(SpacingXL))

                PostActionButton(
                    isEnabled = !interactionState.isLiking,
                    counter = interactionState.likeCount,
                    icon = painterResource(R.drawable.ic_heart_solid),
                    tint = if(interactionState.isLiked) Error else Color.White,
                    onClick = onLike
                )

                PostActionButton(
                    counter = 120,
                    icon = painterResource(R.drawable.ic_clipboard_check_solid),
                    //tint = Color(0xFFF3BA2F),
                    tint = Color.White,
                    onClick = onOpenReviews
                )

                PostActionButton(
                    counter = counters.commentCount,
                    icon = painterResource(R.drawable.ic_comment_solid),
                    onClick = onOpenComments
                )
                PostActionButton(
                    isEnabled = !interactionState.isBookmarking,
                    counter = interactionState.bookmarkCount,
                    icon = painterResource(R.drawable.ic_bookmark_solid),
                    tint = if(interactionState.isBookmarked) Color(0xFFF3BA2F) else Color.White,
                    onClick = onBookmark
                )
                PostActionButton(
                    counter = counters.shareCount,
                    icon = painterResource(R.drawable.ic_send_solid),
                    onClick = onOpenCalendar
                )
            }
        }
    }
}