package com.example.scrollbooker.components.customized.post.sheets.comments.components
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.avatar.Avatar
import com.example.scrollbooker.components.customized.post.sheets.comments.PendingComment
import com.example.scrollbooker.components.customized.post.sheets.comments.PendingStatus
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXXS
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.bodyLarge

@Composable
fun PendingCommentItem(
    pending: PendingComment,
    onRetry: () -> Unit,
    onDiscard: () -> Unit
) {
    val comment = pending.comment
    val isFailed = pending.status == PendingStatus.FAILED

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = BasePadding)
            .padding(bottom = SpacingS)
            .alpha(if (isFailed) 1f else 0.5f),
    ) {
        Avatar(url = comment.user.avatar ?: "", size = CommentAvatarSize)
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
            Text(text = comment.text)
            Spacer(Modifier.height(SpacingXXS))

            if (isFailed) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(BasePadding),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.commentFailedToSend),
                        style = bodyLarge,
                        color = Error
                    )
                    Text(
                        modifier = Modifier
                            .padding(vertical = SpacingS)
                            .clickable(onClick = onRetry),
                        text = stringResource(R.string.retry),
                        style = bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = OnBackground
                    )
                    Text(
                        modifier = Modifier
                            .padding(vertical = SpacingS)
                            .clickable(onClick = onDiscard),
                        text = stringResource(R.string.delete),
                        style = bodyLarge,
                        color = OnBackground
                    )
                }
            } else {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CircularProgressIndicator(modifier = Modifier.size(12.dp))
                    Spacer(Modifier.width(SpacingXXS))
                    Text(
                        text = stringResource(R.string.sendingComment),
                        style = bodyLarge,
                        color = OnBackground
                    )
                }
            }
        }
    }
}
