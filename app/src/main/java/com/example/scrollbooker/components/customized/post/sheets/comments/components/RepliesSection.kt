package com.example.scrollbooker.components.customized.post.sheets.comments.components
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.customized.LoadMoreSpinner
import com.example.scrollbooker.components.customized.post.sheets.comments.CommentLikeState
import com.example.scrollbooker.components.customized.post.sheets.comments.PendingComment
import com.example.scrollbooker.components.customized.post.sheets.comments.PendingStatus
import com.example.scrollbooker.components.customized.post.sheets.comments.RepliesState
import com.example.scrollbooker.core.util.Dimens.AvatarSizeXS
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXS
import com.example.scrollbooker.entity.social.comment.data.remote.LikeCommentEnum
import com.example.scrollbooker.entity.social.comment.domain.model.Comment
import com.example.scrollbooker.navigation.navigators.UserProfileParam
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.bodyLarge

private val RepliesToggleIndent = BasePadding + CommentAvatarSize + BasePadding

@Composable
private fun repliesCountLabel(count: Int): String = if (count == 1) {
    stringResource(R.string.repliesCountOne, count)
} else {
    stringResource(R.string.repliesCountOther, count)
}

@Composable
private fun RepliesToggleRow(
    text: String,
    icon: ImageVector?,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(
                start = RepliesToggleIndent,
                end = BasePadding,
                bottom = BasePadding
            )
            .clickable(
                onClick = onClick,
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HorizontalDivider(
            modifier = Modifier.width(20.dp),
            color = Divider,
            thickness = 1.dp
        )
        Spacer(Modifier.width(SpacingS))
        Text(
            text = text,
            style = bodyLarge,
            color = Color.Gray
        )
        if (icon != null) {
            Icon(
                modifier = Modifier.padding(start = SpacingXS).size(16.dp),
                imageVector = icon,
                contentDescription = null,
                tint = Color.Gray
            )
        }
    }
}

@Composable
fun RepliesSection(
    comment: Comment,
    repliesState: RepliesState,
    pendingReplies: List<PendingComment>,
    likeOverrides: Map<Int, CommentLikeState>,
    onToggle: () -> Unit,
    onLoadMore: () -> Unit,
    onLikeClick: (Comment, LikeCommentEnum) -> Unit,
    onReplyClick: (Comment) -> Unit,
    onRetryComment: (String) -> Unit,
    onDiscardComment: (String) -> Unit,
    onNavigateToUserProfile: (UserProfileParam) -> Unit
) {
    if (comment.repliesCount == 0 && pendingReplies.isEmpty()) return

    if (!repliesState.isExpanded) {
        RepliesToggleRow(
            text = repliesCountLabel(comment.repliesCount + pendingReplies.size),
            icon = Icons.Default.ExpandMore,
            onClick = onToggle
        )
        return
    }

    val loadedReplies = repliesState.items
    val usernameById = (loadedReplies + pendingReplies.map { it.comment } + comment)
        .associate { it.id to it.user.username }

    Column(Modifier.padding(start = AvatarSizeXS)) {
        pendingReplies.forEach { pending ->
            if (pending.status == PendingStatus.SENT) {
                CommentItem(
                    comment = pending.comment.withLikeOverride(likeOverrides),
                    avatarSize = 28.dp,
                    replyToUsername = pending.comment.replyToCommentId?.let { usernameById[it] },
                    onLikeClick = onLikeClick,
                    onReplyClick = onReplyClick,
                    onNavigateToUserProfile = onNavigateToUserProfile
                )
            } else {
                PendingCommentItem(
                    pending = pending,
                    onRetry = { onRetryComment(pending.localId) },
                    onDiscard = { onDiscardComment(pending.localId) }
                )
            }
        }

        loadedReplies.forEach { reply ->
            CommentItem(
                comment = reply.withLikeOverride(likeOverrides),
                avatarSize = 28.dp,
                replyToUsername = reply.replyToCommentId?.let { usernameById[it] },
                onLikeClick = onLikeClick,
                onReplyClick = onReplyClick,
                onNavigateToUserProfile = onNavigateToUserProfile
            )
        }
    }

    if (repliesState.isLoading) {
        LoadMoreSpinner()
    }

    if (!repliesState.isLoading) {
        if (repliesState.hasMore) {
            RepliesToggleRow(
                text = stringResource(R.string.viewMoreReplies),
                icon = null,
                onClick = onLoadMore
            )
        } else {
            RepliesToggleRow(
                text = stringResource(R.string.hideReplies),
                icon = Icons.Default.ExpandLess,
                onClick = onToggle
            )
        }
    }
}
