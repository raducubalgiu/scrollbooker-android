package com.example.scrollbooker.modules.posts.sheets.comments.components
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.components.core.avatar.Avatar
import com.example.scrollbooker.core.util.Dimens.AvatarSizeXS
import com.example.scrollbooker.core.util.Dimens.AvatarSizeXXS
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXS
import com.example.scrollbooker.core.util.Dimens.SpacingXXS
import com.example.scrollbooker.entity.social.comment.data.remote.LikeCommentDto
import com.example.scrollbooker.entity.social.comment.data.remote.LikeCommentEnum
import com.example.scrollbooker.entity.social.comment.domain.model.Comment
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.bodyLarge
import kotlinx.coroutines.delay

@Composable
fun CommentItem(
    comment: Comment,
    onLike: (LikeCommentDto) -> Unit
) {
    var isLiked by remember { mutableStateOf(comment.isLiked) }
    var likeCount by remember { mutableIntStateOf(comment.likeCount) }
    var scale by remember { mutableFloatStateOf(1f) }

    LaunchedEffect(isLiked) {
        scale = if(isLiked) 0.8f else 1.2f
        delay(100)
        scale = 1f
    }

    val animatedScale by animateFloatAsState(
        targetValue = scale,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "iconScale"
    )

    fun handleLike() {
        onLike(
            LikeCommentDto(
            postId = comment.postId,
            commentId = comment.id,
            action = if(isLiked) LikeCommentEnum.UNLIKE else LikeCommentEnum.LIKE)
        )
        isLiked = !isLiked
        if(isLiked) likeCount += 1 else likeCount -= 1
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = BasePadding),
    ) {
        Avatar(
            url = comment.user.avatar ?: "",
            size = AvatarSizeXS
        )
        Spacer(Modifier.width(SpacingS))
        Column {
            Spacer(Modifier.height(SpacingXXS))
            Text(
                style = bodyLarge,
                text = comment.user.username,
                fontWeight = FontWeight.SemiBold,
                color = OnBackground,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(Modifier.height(SpacingXXS))
            Text(text = comment.text)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "2d",
                        style = bodyLarge,
                        fontWeight = FontWeight.Normal,
                        color = Color.Gray
                    )
                    Spacer(Modifier.width(BasePadding))
                    Box(Modifier.padding(vertical = SpacingS)) {
                        Text(
                            text = "Reply",
                            style = bodyLarge,
                            fontWeight = FontWeight.Normal,
                            color = Color.Gray
                        )
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if(comment.likedByPostAuthor) {
                        Avatar(
                            url = "https://media.scrollbooker.ro/frizerie-1-cover.jpg",
                            size = AvatarSizeXXS
                        )
                        Spacer(Modifier.width(BasePadding))
                    }
                    Row(
                        modifier = Modifier.clickable(
                            onClick = { handleLike() },
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AnimatedVisibility(
                            visible = likeCount > 0,
                            enter = fadeIn() + scaleIn(),
                            exit = fadeOut() + scaleOut()
                        ) {
                            Text(
                                text = "${likeCount}",
                                style = bodyLarge,
                                fontWeight = FontWeight.SemiBold,
                                color = if(isLiked) Error else Color.Gray
                            )
                        }
                        Spacer(Modifier.width(SpacingXS))
                        Icon(
                            modifier = Modifier.scale(animatedScale),
                            imageVector = if(isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = null,
                            tint = if(isLiked) Error else Color.Gray
                        )
                    }
                }
            }

            Spacer(Modifier.height(SpacingS))

            if(comment.repliesCount > 0) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HorizontalDivider(Modifier.width(25.dp))
                    Spacer(Modifier.width(BasePadding))
                    Text(
                        text = "Vezi ${comment.repliesCount} raspunsuri",
                        color = Color.Gray
                    )
                }
            }
        }
    }

    Spacer(Modifier.height(BasePadding))
}