package com.example.scrollbooker.modules.posts.common
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.avatar.Avatar
import com.example.scrollbooker.core.util.Dimens.AvatarSizeS
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.util.Dimens.SpacingXXL
import com.example.scrollbooker.entity.post.domain.model.PostCounters
import com.example.scrollbooker.entity.post.domain.model.UserPostActions
import com.example.scrollbooker.modules.posts.PostInteractionState
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.bodyMedium
import com.example.scrollbooker.ui.theme.bodySmall

@Composable
fun PostOverlay(
    interactionState: PostInteractionState,
    counters: PostCounters,
    onLike: () -> Unit,
    onBookmark: () -> Unit,
    onOpenReviews: () -> Unit,
    onOpenComments: () -> Unit
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
                    .background(Color.Yellow)
            ) {
                Text("Hello World")
            }

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(modifier = Modifier
                    .size(AvatarSizeS),
                    contentAlignment = Alignment.BottomCenter)
                {
                    Avatar(
                        url = "https://media.scrollbooker.ro/frizerie-1-cover.jpg",
                        size = AvatarSizeS
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .offset(y = 18.dp)
                            .background(
                                color = Color.Black.copy(alpha = 0.7f),
                                shape = RoundedCornerShape(15.dp)
                            )
                            .padding(horizontal = 6.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_star_solid),
                            contentDescription = "Rating",
                            tint = Primary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(2.dp))
                        Text(
                            text = "4.5",
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                            style = bodySmall,
                        )
                    }
                }

                Spacer(Modifier.height(SpacingXL))

//                PostActionButton(
//                    counter = 120,
//                    icon = Icons.Default.RateReview,
//                    tint = Color(0xFFF3BA2F),
//                    onClick = onOpenReviews
//                )

                PostActionButton(
                    isEnabled = !interactionState.isLiking,
                    counter = interactionState.likeCount,
                    icon = painterResource(R.drawable.ic_heart_solid),
                    tint = if(interactionState.isLiked) Error else Color.White,
                    onClick = onLike
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
                    tint = if(interactionState.isBookmarked) Color.Yellow else Color.White,
                    onClick = onBookmark
                )
                PostActionButton(
                    counter = counters.shareCount,
                    icon = painterResource(R.drawable.ic_send_solid),
                )
            }
        }
    }
}