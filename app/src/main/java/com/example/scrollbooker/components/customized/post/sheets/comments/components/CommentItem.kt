package com.example.scrollbooker.components.customized.post.sheets.comments.components
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.avatar.Avatar
import com.example.scrollbooker.core.extensions.relativeLabel
import com.example.scrollbooker.core.util.Dimens.AvatarSizeXXS
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXS
import com.example.scrollbooker.core.util.Dimens.SpacingXXS
import com.example.scrollbooker.entity.social.comment.data.remote.LikeCommentEnum
import com.example.scrollbooker.entity.social.comment.domain.model.Comment
import com.example.scrollbooker.navigation.navigators.UserProfileParam
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.bodyMedium

val CommentAvatarSize = 35.dp

@Composable
fun CommentItem(
    comment: Comment,
    onLikeClick: (comment: Comment, action: LikeCommentEnum) -> Unit,
    onReplyClick: (comment: Comment) -> Unit,
    onNavigateToUserProfile: (param: UserProfileParam) -> Unit,
    modifier: Modifier = Modifier,
    avatarSize: Dp = CommentAvatarSize,
    replyToUsername: String? = null
) {
    var scale by remember { mutableFloatStateOf(1f) }
    val animatedScale by animateFloatAsState(
        targetValue = scale,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "iconScale"
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = BasePadding)
            .padding(bottom = SpacingS),
    ) {
        Avatar(
            url = comment.user.avatar ?: "",
            size = avatarSize,
            onClick = { onNavigateToUserProfile(
                UserProfileParam(
                    userId = comment.user.id,
                    username = comment.user.username,
                    profession = comment.user.profession
                )
            ) }
        )
        Spacer(Modifier.width(BasePadding))
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
            Text(
                text = if (replyToUsername != null) {
                    buildAnnotatedString {
                        withStyle(SpanStyle(fontWeight = FontWeight.SemiBold, color = OnBackground)) {
                            append("@$replyToUsername ")
                        }
                        append(comment.text)
                    }
                } else {
                    AnnotatedString(comment.text)
                }
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = comment.createdAt.relativeLabel(),
                        style = bodyMedium,
                        fontWeight = FontWeight.Normal,
                        color = Color.Gray
                    )
                    Spacer(Modifier.width(BasePadding))
                    Box(
                        modifier = Modifier
                            .padding(vertical = SpacingS)
                            .clickable(
                                onClick = { onReplyClick(comment) },
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            )
                    ) {
                        Text(
                            text = stringResource(R.string.reply),
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
                            size = 18.dp
                        )
                        Spacer(Modifier.width(BasePadding))
                    }

                    Row(
                        modifier = Modifier.clickable(
                            onClick = {
                                onLikeClick(
                                    comment,
                                    if (comment.isLiked) LikeCommentEnum.UNLIKE else LikeCommentEnum.LIKE
                                )
                            },
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AnimatedVisibility(
                            visible = comment.likeCount > 0,
                            enter = fadeIn() + scaleIn(),
                            exit = fadeOut() + scaleOut()
                        ) {
                            Text(
                                text = "${comment.likeCount}",
                                style = bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = if(comment.isLiked) Error else Color.Gray
                            )
                        }
                        Spacer(Modifier.width(SpacingXS))
                        Icon(
                            modifier = Modifier
                                .size(18.dp)
                                .scale(animatedScale),
                            imageVector = if(comment.isLiked) Icons.Default.Favorite
                                          else Icons.Default.FavoriteBorder,
                            contentDescription = null,
                            tint = if(comment.isLiked) Error else Color.Gray
                        )
                    }
                }
            }
        }
    }
}
