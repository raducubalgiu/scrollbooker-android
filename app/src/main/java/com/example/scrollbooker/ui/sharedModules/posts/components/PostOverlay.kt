package com.example.scrollbooker.ui.sharedModules.posts.components
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.avatar.AvatarWithRating
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.entity.social.post.domain.model.PostCounters
import com.example.scrollbooker.ui.sharedModules.posts.PostInteractionState
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.OnPrimary
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.bodyMedium

@Composable
fun PostOverlay(
    interactionState: PostInteractionState,
    counters: PostCounters,
    onLike: () -> Unit,
    onBookmark: () -> Unit,
    onOpenReviews: () -> Unit,
    onOpenComments: () -> Unit,
    onOpenCalendar: () -> Unit,
    onOpenLocation: () -> Unit
) {
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(
            top = SpacingS,
            start = SpacingS,
            bottom = SpacingS
        )
        .zIndex(3f),
        contentAlignment = Alignment.BottomCenter
    ) {
        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Button(
                    onClick = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = SpacingXL),
                    shape = ShapeDefaults.Medium,
                    contentPadding = PaddingValues(
                        vertical = 11.dp,
                        horizontal = BasePadding
                    ),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = OnPrimary,
                        containerColor = Primary.copy(0.9f)
                    ),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = onOpenCalendar
                            ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            modifier = Modifier.size(20.dp),
                            painter = painterResource(R.drawable.ic_calendar_outline),
                            contentDescription = null,
                            tint = OnPrimary
                        )

                        Spacer(Modifier.width(BasePadding))

                        Text(
                            style = bodyMedium,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            text = "Intervale disponibile"
                        )

                        Icon(
                            modifier = Modifier.size(20.dp),
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = null,
                            tint = OnPrimary
                        )
                    }
                }
            }

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AvatarWithRating(
                    rating = "4.5",
                    size = 60.dp
                )

                Spacer(Modifier.height(SpacingM))

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
                    onClick = onOpenLocation
                )
            }
        }
    }
}