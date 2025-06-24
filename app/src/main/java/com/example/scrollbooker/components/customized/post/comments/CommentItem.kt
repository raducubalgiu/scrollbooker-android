package com.example.scrollbooker.components.customized.post.comments
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
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.components.core.avatar.Avatar
import com.example.scrollbooker.core.util.Dimens.AvatarSizeXXS
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXS
import com.example.scrollbooker.core.util.Dimens.SpacingXXS
import com.example.scrollbooker.shared.comment.domain.model.Comment
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.bodyLarge

@Composable
fun CommentItem(comment: Comment) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = BasePadding),
    ) {
        Avatar(
            url = comment.user.avatar ?: "",
            size = AvatarSizeXXS
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
                Column {
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
                    if(comment.likeCount  > 0) {
                        Text(
                            modifier = Modifier.padding(end = SpacingXS),
                            text = "2",
                            style = bodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = if(comment.isLiked) Error else Color.Gray
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = null,
                        tint = if(comment.isLiked) Error else Color.Gray
                    )
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