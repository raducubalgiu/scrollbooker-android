package com.example.scrollbooker.modules.post.common
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ModeComment
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.components.core.avatar.Avatar
import com.example.scrollbooker.core.util.Dimens.SpacingXXL
import com.example.scrollbooker.entity.post.domain.model.PostCounters
import com.example.scrollbooker.entity.post.domain.model.UserPostActions
import com.example.scrollbooker.ui.theme.Primary

@Composable
fun PostOverlay(
    userActions: UserPostActions,
    counters: PostCounters,
    onOpenReviews: () -> Unit,
    onOpenComments: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.weight(1f)) {}

            Column {
                Box(modifier = Modifier.size(64.dp), contentAlignment = Alignment.BottomCenter) {
                    Avatar(
                        url = "https://media.scrollbooker.ro/frizerie-1-cover.jpg",
                        size = 55.dp
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .offset(y = 18.dp)
                            .background(
                                color = Color.Black.copy(alpha = 0.7f),
                                shape = RoundedCornerShape(15.dp)
                            )
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Rating",
                            tint = Primary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(2.dp))
                        Text(
                            text = "4.5",
                            color = Color.White,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(Modifier.height(SpacingXXL))

                PostActionButton(
                    isAction = false,
                    counter = 120,
                    icon = Icons.Default.RateReview,
                    tint = Color(0xFFF3BA2F),
                    onClick = onOpenReviews
                )

                PostActionButton(
                    isAction = userActions.isLiked,
                    counter = counters.likeCount,
                    icon = Icons.Default.Favorite,
                )
                PostActionButton(
                    isAction = false,
                    counter = counters.commentCount,
                    icon = Icons.Default.ModeComment,
                    onClick = onOpenComments
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